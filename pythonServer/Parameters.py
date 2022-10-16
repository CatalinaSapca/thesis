import argparse

imagenet_16384 = True

width =  300
height =  300
images_interval =  50
seed = None
max_iterations = 5

model = "vqgan_imagenet_f16_16384"
model_names={"vqgan_imagenet_f16_16384": 'ImageNet 16384'}
name_model = model_names[model]

args = argparse.Namespace(
    # prompts=texts,
    # image_prompts=target_images,
    image_prompts=[],
    noise_prompt_seeds=[],
    noise_prompt_weights=[],
    size=[width, height],
    # init_image=init_image,
    #init_image=[],
    init_weight=0.,
    clip_model='ViT-B/32',
    vqgan_config=f'{model}.yaml',
    vqgan_checkpoint=f'{model}.ckpt',
    step_size=0.1,
    cutn=64,
    cut_pow=1.,
    display_freq=images_interval,
    seed=seed,
)