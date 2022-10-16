package com.thesis.tattootopy.model;

import com.thesis.tattootopy.modelDTO.UserDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "email")
    private String email;

    private String hashedPassword;

    private String firstName;

    private String lastName;

    @OneToMany
    ///manytomany before
    @JoinTable(name = "user_tattoos", joinColumns = @JoinColumn(name = "user_email"), inverseJoinColumns = @JoinColumn(name = "tattoo_id"))
    private List<Tattoo> tattoos = null;

    @ColumnDefault("-1")
    private String token;

    // ----------------------------------------------------------------------- CONSTRUCTORS

    public User() {
    }

    public User(String email) {
        this.email = email;
    }

    public User(String email, String hashedPassword) {
        this.email = email;
        this.hashedPassword = hashedPassword;
    }

    public User(String email, String hashedPassword, String firstName, String lastName) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(String email, String hashedPassword, String firstName, String lastName, String token) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public List<Tattoo> getUserTattoos() {
        return tattoos;
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

    public void setUserTattoos(List<Tattoo> tattoos) {
        this.tattoos = tattoos;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void addTattoo(Tattoo tattoo){
        if(tattoos == null)
            tattoos = new ArrayList<>();
        tattoos.add(tattoo);
    }

    public void removeTattoo(Tattoo tattoo){
        if(tattoos != null)
            tattoos.remove(tattoo);
    }

    // ----------------------------------------------------------------------- OTHERS

    @Override
    public String toString() {
        return "User[" + ", email='" + email + ", password='" + hashedPassword + ", firstName='" + firstName + ", lastName='" + lastName + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User that = (User) o;

//        return getBirthDate().equals(that.getBirthDate()) &&
//                getFirstName().equals(that.getFirstName()) &&
//                getLastName().equals(that.getLastName()) &&
//                getEmail().equals(that.getEmail());
        return getEmail().equals(that.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail(), getFirstName(), getLastName());
    }

    public UserDTO toDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        userDTO.setHashedPassword(hashedPassword);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);

        return userDTO;
    }


}

