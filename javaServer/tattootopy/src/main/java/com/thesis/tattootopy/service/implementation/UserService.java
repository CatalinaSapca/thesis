package com.thesis.tattootopy.service.implementation;

import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.model.User;
import com.thesis.tattootopy.persistance.interfaces.IUserRepository;
import com.thesis.tattootopy.service.interfaces.IUserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    private static final Logger logger = LogManager.getLogger();

    /**
     * Returns a list of all Users from the database.
     *
     * @return List<User>
     */
    @Override
    public List<User> getAllUsers() {
        logger.info("UserService : getAllUsers() is called");
        return userRepository.findAll();
    }

    /**
     * Returns a list of all Tattoos of a User
     *
     * @return List<Tattoo>
     */
    @Override
    public List<Tattoo> findAllTattoosOfUser(String email) {
        logger.info("UserService : findAllTattoosOfUser() is called");
        Optional<User> user = userRepository.findById(email);
        return user.get().getUserTattoos();
    }

    /**
     * Returns the User that has the email given in the parameters
     * If no User with that email was found, returns null.
     *
     * @param email - a String, not null
     * @return the User with the given email (id) or null if none found.
     */
    @Override
    public User findUserByEmail(String email) {
        logger.info("UserService : findUserByEmail( " + email + " ) is called");

        Optional<User> user = userRepository.findById(email);
        return user.orElse(null);
    }

    /**
     * Saves the given user in the database
     * Returns true if the entity was saved successfully (meaning the user doesn't already exist);
     * Returns false if an entity with the user's email already EXISTS.
     *
     * @param user - a User, not null
     * @return true if the user was saved, false otherwise
     */
    @Override
    public boolean save(User user) {
        logger.info("UserService : save( " + user + " ) is called");

        Optional<User> fromDatabase = userRepository.findById(user.getEmail());
        if (fromDatabase.isPresent()) {
            // user ALREADY exists -> return false
            logger.info("UserService : user - " + fromDatabase.get().getEmail() + " could not be saved because it ALREADY EXISTS.");
            return false;
        } else {
            // we are adding the user -> return true
            userRepository.save(user);
            logger.info("UserService : user - " + user.getEmail() + " was saved.");
            return true;
        }
    }

    /**
     * Updates the uses that has the given user's email.
     * Returns true if the entity was updates successfully (meaning the user already exists in the database);
     * Returns false if NO entity with the user's email was found.
     *
     * @param user - a User, not null
     * @return true if the user was updated, false otherwise
     */
    @Override
    public boolean update(User user) {
        logger.info("UserService : update( " + user + " ) is called");

        try {
            User fromDB = userRepository.getById(user.getEmail());
            // if no exception was thrown -> user was found -> we can update it now -> return true
            fromDB.setFirstName(user.getFirstName());
            fromDB.setLastName(user.getLastName());
            userRepository.save(user);
            logger.info("UserService : user - " + user.getEmail() + " was updated.");
            return true;

        } catch (EntityNotFoundException ex) {
            // user DOESN'T exist -> we can't update it
            logger.info("UserService : user - " + user.getEmail() + " could not be updated because it DOESN'T exist..");
            return false;
        }
    }

    /**
     * Deletes user that has given email.
     *
     * @param email - a String, not null
     * @return true if the user with the given email was deleted, false otherwise
     */
    @Override
    public boolean deleteUserByEmail(String email) {
        logger.info("UserService : delete( " + email + " ) is called");

        Optional<User> fromDatabase = userRepository.findById(email);
        if (fromDatabase.isPresent()) {
            // user was found -> can be deleted
            userRepository.deleteById(email);
            logger.info("UserService : user - " + email + " was dleted.");
            return true;
        } else {
            // user was not found -> can not be deleted
            logger.info("UserService : user - " + email + " could not be deleted bcs it DOESN'T exist.");
            return false;
        }
    }

}

