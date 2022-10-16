package com.thesis.tattootopy.model.validators;

import com.thesis.tattootopy.model.Tattoo;

import java.security.InvalidParameterException;

/**
 * Validates an instance of class Tattoo
 * Throws ValidateException if instance is invalid
 */
public class TattooValidator implements Validator<Tattoo>{
    /**
     * @throws InvalidParameterException if id is null or <=0 / path/description are null.
     */
    @Override
    public void validate(Tattoo entity) throws ValidationException {
        if (entity == null)
            throw new InvalidParameterException();
        else {
            if (entity.getId() == null)
                throw new ValidationException("Invalid id!");
            if (entity.getPath() == null)
                throw new ValidationException("Invalid path!");
            if (entity.getDescription() == null)
                throw new ValidationException("Invalid description!");
        }
    }
}