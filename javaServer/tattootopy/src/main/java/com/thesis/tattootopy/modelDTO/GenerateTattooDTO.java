package com.thesis.tattootopy.modelDTO;

import com.thesis.tattootopy.model.Tattoo;

import java.io.Serializable;
import java.util.Objects;

public class GenerateTattooDTO  implements Serializable {

    private String userEmailAddress;
    private String description;

    // ----------------------------------------------------------------------- CONSTRUCTORS
    public GenerateTattooDTO() {
    }

    public GenerateTattooDTO(String userEmailAddress, String description) {
        this.userEmailAddress = userEmailAddress;
        this.description = description;
    }

    // ----------------------------------------------------------------------- GETTERS

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public String getDescription() {
        return description;
    }


    // ----------------------------------------------------------------------- SETTERS

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // ----------------------------------------------------------------------- OTHERS

    @Override
    public String toString() {
        return "GenerateTattooDTO{" + "userEmailAddress=" + userEmailAddress + ", description='" + description + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenerateTattooDTO)) return false;
        GenerateTattooDTO that = (GenerateTattooDTO) o;
        return getUserEmailAddress().equals(that.getUserEmailAddress()) &&
                getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserEmailAddress(), getDescription());
    }



}

