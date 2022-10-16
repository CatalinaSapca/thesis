package com.thesis.tattootopy.service.implementation;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thesis.tattootopy.TattootopyApplication;
import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.persistance.interfaces.ITattooRepository;
import com.thesis.tattootopy.service.interfaces.ITattooService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Service;
import sun.net.www.http.HttpClient;

import javax.mail.MessagingException;
import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class TattooService implements ITattooService {

    @Autowired
    private ITattooRepository tattooRepository;

    private static final Logger logger = LogManager.getLogger();

    /**
     * Returns a list of all Tattoos from the database.
     *
     * @return List<Tattoo>
     */
    @Override
    public List<Tattoo> getAllTattoos() {
        logger.info("TattooService : getAllTattoos() is called");
        return tattooRepository.findAll();
    }

    /**
     * Returns a list of all Tattoos from the database that have a description that contains the like string or substring
     *
     * @return List<Tattoo>
     */
    @Override
    public List<Tattoo> findAllTattoosWithDescriptionLike(String like) {
        logger.info("TattooService : findAllTattoosWithDescriptionLike() is called");
        List<Tattoo> tattoos = new ArrayList<>();
        tattooRepository.findAll().forEach(tattoo -> {
            if (tattoo.getDescription().contains(like) && tattoo.isPublic())
                tattoos.add(tattoo);
        });
        return tattoos;
    }

    /**
     * Returns a list of all Demo Tattoos from the database
     *
     * @return List<Tattoo>
     */
    @Override
    public List<Tattoo> findAllDemoTattoos() {
        logger.info("TattooService : findAllDemoTattoo() is called");
        List<Tattoo> tattoos = new ArrayList<>();
        tattooRepository.findAll().forEach(tattoo -> {
            if (tattoo.isDemo())
                tattoos.add(tattoo);
        });
        return tattoos;
    }


    /**
     * Returns the Tattoo that has the id given in the parameters
     * If no Tattoo with that id was found, returns null.
     *
     * @param id - a Long, not null
     * @return the Tattoo with the given id or null if none found.
     */
    @Override
    public Tattoo findTattooById(Long id) {
        logger.info("TattooService : findTattooById( " + id + " ) is called");

        Optional<Tattoo> tattoo = tattooRepository.findById(id);
        return tattoo.orElse(null);
    }

    /**
     * Saves the given tattoo in the database
     * Returns true if the entity was saved successfully (meaning the tattoo doesn't already exist);
     * Returns false if an entity with the tattoo's id already EXISTS.
     *
     * @param tattoo - a Tattoo, not null
     * @return true if the tattoo was saved, false otherwise
     */
    @Override
    public Tattoo save(Tattoo tattoo) {
        logger.info("TattooService : save( " + tattoo + " ) is called");

        // if id is null, tattoo surely doesn't exist in the database
        if(tattoo.getId() == null){
            // we are adding the tattoo -> return true
            Tattoo savedEntity = tattooRepository.save(tattoo);
            logger.info("TattooService : tattoo - " + tattoo.getId() + " was saved.");
            return savedEntity;
        }
        else {
            Optional<Tattoo> fromDatabase = tattooRepository.findById(tattoo.getId());
            if (fromDatabase.isPresent()) {
                // tattoo ALREADY exists -> return false
                logger.info("TattooService : tattoo - " + fromDatabase.get().getId() + " could not be saved because it ALREADY EXISTS.");
                return fromDatabase.get();
            } else {
                // we are adding the tattoo -> return true
                tattooRepository.save(tattoo);
                logger.info("TattooService : tattoo - " + tattoo.getId() + " was saved.");
                return null;
            }
        }
    }

    /**
     * Updates the tattoo that has the given user's email.
     * Returns true if the entity was updates successfully (meaning the user already exists in the database);
     * Returns false if NO entity with the tattoo's id was found.
     *
     * @param tattoo - a Tattoo, not null
     * @return true if the user was updated, false otherwise
     */
    @Override
    public boolean update(Tattoo tattoo) {
        logger.info("TattooService : update( " + tattoo + " ) is called");

        try {
            Tattoo fromDB = tattooRepository.getById(tattoo.getId());
            // if no exception was thrown -> tattoo was found -> we can update it now -> return true
            //fromDB.setDescription(tattoo.getDescription());
            fromDB.setPublic(tattoo.isPublic());

            tattooRepository.save(fromDB);
            logger.info("TattooService : tattoo - " + tattoo.getId() + " was updated.");
            return true;

        } catch (EntityNotFoundException ex) {
            // tattoo DOESN'T exist -> we can't update it
            logger.info("TattooService : tattoo - " + tattoo.getId() + " could not be updated because it DOESN'T exist..");
            return false;
        }
    }

    /**
     * Deletes tattoo that has given email.
     *
     * @param id - a Long, not null
     * @return true if the tattoo with the given id was deleted, false otherwise
     */
    @Override
    public boolean deleteTattooById(Long id) {
        logger.info("TattooService : delete( " + id + " ) is called");

        Optional<Tattoo> fromDatabase = tattooRepository.findById(id);
        if (fromDatabase.isPresent()) {
            // tattoo was found -> can be deleted
            tattooRepository.deleteById(id);
            logger.info("TattooService : tattoo - " + id + " was deleted.");
            return true;
        } else {
            // tattoo was not found -> can not be deleted
            logger.info("TattooService : tattoo - " + id + " could not be deleted bcs it DOESN'T exist.");
            return false;
        }
    }


    @Override
    public void generateTattoo(Tattoo tattoo, String userEmailAddress, String description, String startImagePath) throws IOException {
//        TattooGeneratorRunnable tattooGeneratorRunnable = new TattooGeneratorRunnable(tattoo, userEmailAddress, description, tattooRepository);
//        Thread t = new Thread(tattooGeneratorRunnable);
//        t.start();

        URL url = new URL ("http://127.0.0.1:4567/tattoo-generation");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        String jsonInputString = "{\"description\": \"" + description + "\", \"startImagePath\": \"" + startImagePath + "\", \"uniqueTattooId\":"+  tattoo.getId() + "}";
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        StringBuilder response = null;
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
        }

        // ------------------ parsam json-ul din stringul primit de la serverul Python

        JsonObject jsonObject = new JsonParser().parse(String.valueOf(response)).getAsJsonObject();

        String finalImagePath = jsonObject.get("finalImagePath").getAsString();
        System.out.println(finalImagePath);

        // ------------------ updatam tattoo cu locatia unde este salvata imaginea generata
        // ------------------ si trimitem email catre user
        try {
            Optional<Tattoo> fromDB = tattooRepository.findById(tattoo.getId());
            if (fromDB.isPresent()) {
                fromDB.get().setDescription(tattoo.getDescription());
                fromDB.get().setPath(finalImagePath);
                fromDB.get().setPublic(tattoo.isPublic());
                fromDB.get().setDemo(tattoo.isDemo());

                tattooRepository.save(fromDB.get());



                MailSenderHelper mailSenderHelper = MailSenderHelper.getMailSenderHelperInstance();

                
                //mailSenderHelper.sendmail(userEmailAddress, finalImagePath.replace("\n", "").replace("\r", ""));
                mailSenderHelper.sendmail(userEmailAddress, "../../../../../opt/lampp/htdocs/tattootopyImages/steps/final/" + tattoo.getId() + ".png");
            }

        } catch (EntityNotFoundException ex) {
            // tattoo COULDN'T be found in the database
            // should never reach this
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }

    }

}

