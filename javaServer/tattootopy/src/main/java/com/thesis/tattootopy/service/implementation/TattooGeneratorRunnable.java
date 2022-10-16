package com.thesis.tattootopy.service.implementation;

import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.persistance.interfaces.ITattooRepository;

import javax.mail.*;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.Optional;

public class TattooGeneratorRunnable implements Runnable {

    private Tattoo tattoo;
    private String userEmailAddress;
    private String description;
    private ITattooRepository tattooRepository;

    public TattooGeneratorRunnable(Tattoo tattoo, String userEmailAddress, String description, ITattooRepository tattooRepository) {
        this.tattoo = tattoo;
        this.userEmailAddress = userEmailAddress;
        this.description = description;
        this.tattooRepository = tattooRepository;
    }

    public void run() {
        // aici se ruleaza scriptul python
        // se genereaza o imagine cu un tatuaj, se salveaza in database
        // should return the generated tattoo

//        Tattoo tattoo = new Tattoo(description);
//        Tattoo returned = tattooRepository.save(tattoo);

        String filePath = SocketHelper.generate(tattoo.getId(), description);

        try {
            Optional<Tattoo> fromDB = tattooRepository.findById(tattoo.getId());
            if (fromDB.isPresent()) {
                fromDB.get().setDescription(tattoo.getDescription());
                fromDB.get().setPath(filePath);
                fromDB.get().setPublic(tattoo.isPublic());
                fromDB.get().setDemo(tattoo.isDemo());

                tattooRepository.save(fromDB.get());

//                System.out.println(filePath);
//                System.out.println(filePath.replace("\n", "").replace("\r", ""));
                MailSenderHelper mailSenderHelper = MailSenderHelper.getMailSenderHelperInstance();
                mailSenderHelper.sendmail(userEmailAddress, filePath.replace("\n", "").replace("\r", ""));
            }

        } catch (EntityNotFoundException ex) {
            // tattoo COULDN'T be found in the database
            // should never reach this
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }





}
