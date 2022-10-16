package com.thesis.tattootopy.model.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
