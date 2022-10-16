package com.thesis.tattootopy.model.validators;

import com.thesis.tattootopy.model.User;

import java.security.InvalidParameterException;

/**
 * Validates an instance of class User
 * Throws ValidateException if instance is invalid
 */
public class UserValidator implements Validator<User>{
    /**
     * @throws InvalidParameterException if id is null or <=0 / email/first name/last name are null.
     */
    @Override
    public void validate(User entity) throws ValidationException {
        if (entity == null)
            throw new InvalidParameterException();
        else {
            if (entity.getEmail() == null)
                throw new ValidationException("Invalid email!");
            if (entity.getFirstName() == null)
                throw new ValidationException("Invalid first name!");
            if (entity.getLastName() == null)
                throw new ValidationException("Invalid last name!");
        }
    }
}
