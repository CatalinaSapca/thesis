import os
import threading
import asyncio
from flask import Flask, jsonify, request

from finalShit.LoadLib import download_img, load_vqgan_model, MakeCutouts, parse_prompt, Prompt, resize_image, \
    vector_quantize, clamp_with_grad
import wget

from finalShit.Parameters import args, name_model, seed, max_iterations

# --------------------------------------------------------------------------- LIBRARIES required by the VQGAN+CLIP algorithms

import argparse
import math
from concurrent.futures import ThreadPoolExecutor
from pathlib import Path
import sys

import wget

from IPython import display
from base64 import b64encode
from omegaconf import OmegaConf
from PIL import Image
from taming.models import cond_transformer, vqgan
import torch
from torch import nn, optim
from torch.nn import functional as F
from torchvision import transforms
from torchvision.transforms import functional as TF
from tqdm.notebook import tqdm

from CLIP import clip
import kornia.augmentation as K
import numpy as np
import imageio
from PIL import ImageFile, Image
from imgtag import ImgTag  # metadatos
from libxmp import *  # metadatos
import libxmp  # metadatos
from stegano import lsb
import json

# --------------------------------------------------------------------------- LIBRARIES required by the VQGAN+CLIP algorithms

print(f"In flask global level: {threading.current_thread().name}")


# --------------------------------------------------------------------------- INITIALIZATION

ImageFile.LOAD_TRUNCATED_IMAGES = True

#def before():
device = torch.device('cuda:0' if torch.cuda.is_available() else 'cpu')
print('Using device:', device)

model = load_vqgan_model(args.vqgan_config, args.vqgan_checkpoint).to(device)
perceptor = clip.load(args.clip_model, jit=False)[0].eval().requires_grad_(False).to(device)

cut_size = perceptor.visual.input_resolution

e_dim = model.quantize.e_dim #embedding dimension = 256

f = 2 ** (model.decoder.num_resolutions - 1)
print(f)
make_cutouts = MakeCutouts(cut_size, args.cutn, cut_pow=args.cut_pow)
n_toks = model.quantize.n_e #number of embedding

toksX, toksY = args.size[0] // f, args.size[1] // f
sideX, sideY = toksX * f, toksY * f

z_min = model.quantize.embedding.weight.min(dim=0).values[None, :, None, None]
z_max = model.quantize.embedding.weight.max(dim=0).values[None, :, None, None]

normalize = transforms.Normalize(mean=[0.48145466, 0.4578275, 0.40821073],
                                 std=[0.26862954, 0.26130258, 0.27577711])

# --------------------------------------------------------------------------- FINISH INITIALIZATION

app = Flask(__name__)

# --------------------------------------------------------------------------- FUNCTIONS

async def generate(description, start_image, use_start_image, uniqueId):



    try:
        os.mkdir(f"../../../../../opt/lampp/htdocs/tattootopyImages/steps/{threading.current_thread().name}")
    except OSError as error:
        print(error)



    if start_image == "None":
        start_image = None
    elif start_image and start_image.lower().startswith("http"):
        start_image = download_img(start_image)

    texts = [frase.strip() for frase in description.split("|")]
    if texts == ['']:
        texts = []

    if texts:
        print('Using texts:', texts)

    if args.seed is None:
        seed = torch.seed()
    else:
        seed = args.seed
    torch.manual_seed(seed)
    print('Using seed:', seed)

    #device, model, perceptor, cut_size, e_dim, f, make_cutouts, n_toks, toksX, toksY, sideX, sideY, z_min, z_max = before()


    if start_image:
        pil_image = Image.open(start_image).convert('RGB')
        pil_image = pil_image.resize((sideX, sideY), Image.LANCZOS)
        z, *_ = model.encode(TF.to_tensor(pil_image).to(device).unsqueeze(0) * 2 - 1)
    else:
        one_hot = F.one_hot(torch.randint(n_toks, [toksY * toksX], device=device), n_toks).float()
        z = one_hot @ model.quantize.embedding.weight
        z = z.view([-1, toksY, toksX, e_dim]).permute(0, 3, 1, 2)
    z_orig = z.clone()
    z.requires_grad_(True)
    opt = optim.Adam([z], lr=args.step_size)

    pMs = []

    for prompt in texts:
        txt, weight, stop = parse_prompt(prompt)
        print(prompt+" "+txt+" "+" " )
        print(stop)
        print(weight)
        embed = perceptor.encode_text(clip.tokenize(txt).to(device)).float()
        pMs.append(Prompt(embed, weight, stop).to(device))
    # for prompt in texts:
    #     words = texts = [frase.strip() for frase in prompt.split(" ")]
    #     for word in words:
    #         txt, weight, stop = parse_prompt(word)
    #         print(word+" "+txt+" "+" " )
    #         print(stop)
    #         print(weight)
    #         embed = perceptor.encode_text(clip.tokenize(txt).to(device)).float()
    #         pMs.append(Prompt(embed, weight, stop).to(device))

    for prompt in args.image_prompts:
        path, weight, stop = parse_prompt(prompt)
        img = resize_image(Image.open(path).convert('RGB'), (sideX, sideY))
        batch = make_cutouts(TF.to_tensor(img).unsqueeze(0).to(device))
        embed = perceptor.encode_image(normalize(batch)).float()
        pMs.append(Prompt(embed, weight, stop).to(device))

    for seed, weight in zip(args.noise_prompt_seeds, args.noise_prompt_weights):
        gen = torch.Generator().manual_seed(seed)
        embed = torch.empty([1, perceptor.visual.output_dim]).normal_(generator=gen)
        pMs.append(Prompt(embed, weight).to(device))

    i = 0
    try:
        with tqdm() as pbar:
            while True:
                train(i, opt, use_start_image, z, z_orig, pMs, texts, uniqueId)
                if i == max_iterations:
                    break
                i += 1
                pbar.update()
    except KeyboardInterrupt:
        pass

    path = f"../../../../../opt/lampp/htdocs/tattootopyImages/steps/final/{uniqueId}.png"
    print(path)


def synth(z):
    z_q = vector_quantize(z.movedim(1, 3), model.quantize.embedding.weight).movedim(3, 1)
    return clamp_with_grad(model.decode(z_q).add(1).div(2), 0, 1)


def add_xmp_data(nombrefichero, use_start_image, i, texts):
    imagen = ImgTag(filename=nombrefichero)
    imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'creator', 'VQGAN+CLIP',
                                 {"prop_array_is_ordered": True, "prop_value_is_array": True})
    if texts:
        imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'title', " | ".join(texts),
                                     {"prop_array_is_ordered": True, "prop_value_is_array": True})
    else:
        imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'title', 'None',
                                     {"prop_array_is_ordered": True, "prop_value_is_array": True})
    imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'i', str(i),
                                 {"prop_array_is_ordered": True, "prop_value_is_array": True})
    imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'model', name_model,
                                 {"prop_array_is_ordered": True, "prop_value_is_array": True})
    imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'seed', str(seed),
                                 {"prop_array_is_ordered": True, "prop_value_is_array": True})
    imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'input_images', str(use_start_image),
                                 {"prop_array_is_ordered": True, "prop_value_is_array": True})
    # for frases in args.prompts:
    #    imagen.xmp.append_array_item(libxmp.consts.XMP_NS_DC, 'Prompt' ,frases, {"prop_array_is_ordered":True, "prop_value_is_array":True})
    imagen.close()


def add_stegano_data(filename, use_start_image, i, texts):
    data = {
        "title": " | ".join(texts) if texts else None,
        "notebook": "VQGAN+CLIP",
        "i": i,
        "model": name_model,
        "seed": str(seed),
        "input_images": use_start_image
    }
    lsb.hide(filename, json.dumps(data)).save(filename)


@torch.no_grad()
def checkin(i, losses, use_start_image, z, texts):
    losses_str = ', '.join(f'{loss.item():g}' for loss in losses)
    tqdm.write(f'i: {i}, loss: {sum(losses).item():g}, losses: {losses_str}')
    out = synth(z)
    TF.to_pil_image(out[0].cpu()).save('progress.png')
    add_stegano_data('progress.png', use_start_image, i, texts)
    add_xmp_data('progress.png', use_start_image, i, texts)
    display.display(display.Image('progress.png'))


def ascend_txt(use_start_image, z, z_orig, pMs, i, texts, uniqueId):
    out = synth(z)
    iii = perceptor.encode_image(normalize(make_cutouts(out))).float()

    result = []

    if args.init_weight:
        result.append(F.mse_loss(z, z_orig) * args.init_weight / 2)

    for prompt in pMs:
        result.append(prompt(iii))
    img = np.array(out.mul(255).clamp(0, 255)[0].cpu().detach().numpy().astype(np.uint8))[:, :, :]
    img = np.transpose(img, (1, 2, 0))
    # filename = f"steps/{i:04}.png"
    # try:
    #     os.mkdir(f"../../../../../opt/lampp/htdocs/tattootopyImages/steps/{threading.current_thread().name}")
    # except OSError as error:
    #     print(error)


    filename = f"../../../../../opt/lampp/htdocs/tattootopyImages/steps/{threading.current_thread().name}/{i:04}.png"
    imageio.imwrite(filename, np.array(img))
    add_stegano_data(filename, use_start_image, i, texts)
    add_xmp_data(filename, use_start_image, i, texts)

    if i == max_iterations:
        imageio.imwrite(f"../../../../../opt/lampp/htdocs/tattootopyImages/steps/final/" + str(uniqueId) + ".png", np.array(img))

    return result


def train(i, opt, use_start_image, z, z_orig, pMs, texts, uniqueId):
    opt.zero_grad()
    lossAll = ascend_txt(use_start_image, z, z_orig, pMs, i, texts, uniqueId)
    if i % args.display_freq == 0:
        checkin(i, lossAll, use_start_image, z, texts)
    loss = sum(lossAll)
    loss.backward()
    opt.step()
    with torch.no_grad():
        z.copy_(z.maximum(z_min).minimum(z_max))

# --------------------------------------------------------------------------- END FUNCTIONS

@app.route("/tattoo-generation", methods=["POST"])
def index():
    print(f"Inside flask function: {threading.current_thread().name}")
    # asyncio.set_event_loop(asyncio.new_event_loop())
    # loop = asyncio.get_event_loop()
    # result = loop.run_until_complete(generate("a house", "../../../../../opt/lampp/htdocs/tattootopyImages/steps/0200.png", True, 1))

    data = request.json
    description = data["description"]
    startImagePath = data["startImagePath"]
    uniqueId = data["uniqueTattooId"]
    print(description)
    print(startImagePath)
    print(uniqueId)

    asyncio.set_event_loop(asyncio.new_event_loop())
    loop = asyncio.get_event_loop()

    if startImagePath == "":
        result = loop.run_until_complete(generate(description, "", True, uniqueId))
    else:
        result = loop.run_until_complete(generate(description, startImagePath, False, uniqueId))

    return jsonify({"finalImagePath": f"http://localhost/tattootopyImages/steps/final/{uniqueId}.png"})


async def hello():
    await asyncio.sleep(5)
    return 1


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=4567, debug=False)