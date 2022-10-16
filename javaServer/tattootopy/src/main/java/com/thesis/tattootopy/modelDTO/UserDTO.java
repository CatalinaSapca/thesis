package com.thesis.tattootopy.modelDTO;

import com.thesis.tattootopy.model.User;

import java.io.Serializable;
import java.util.Objects;

public class UserDTO  implements Serializable {

    private String email;
    private String hashedPassword;
    private String firstName;
    private String lastName;

    // ----------------------------------------------------------------------- CONSTRUCTORS
    public UserDTO() {
    }

    public UserDTO(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public UserDTO(String email, String hashedPassword, String firstName, String lastName) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // ----------------------------------------------------------------------- GETTERS

    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    // ----------------------------------------------------------------------- SETTERS

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // ----------------------------------------------------------------------- OTHERS

    @Override
    public String toString() {
        return "UserDTO{" + "email=" + email + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO that = (UserDTO) o;
        return getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

    public User toUser(){
        User user = new User();
        user.setEmail(email);
        user.setHashedPassword(hashedPassword);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        return user;
    }


}

