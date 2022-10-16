package com.thesis.tattootopy.model;

import com.thesis.tattootopy.modelDTO.TattooDTO;
import com.thesis.tattootopy.modelDTO.UserDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "tattoos")
public class Tattoo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    private String description;

    private String path;

    private boolean isPublic;

    private boolean isDemo;

    @OneToOne
    ///manytomany before
    @JoinTable(name = "user_tattoos", joinColumns = @JoinColumn(name = "tattoo_id"), inverseJoinColumns = @JoinColumn(name = "user_email"))
    private User user = null;

    // ----------------------------------------------------------------------- CONSTRUCTOR

    public Tattoo() {
    }

    public Tattoo(String description) {
        this.description = description;
    }

    public Tattoo(Long id, String path) {
        this.id = id;
        this.path = path;
    }

    public Tattoo(Long id, String description, String path) {
        this.id = id;
        this.description = description;
        this.path = path;
    }

    public Tattoo(Long id, String description, String path, boolean isPublic) {
        this.id = id;
        this.description = description;
        this.path = path;
        this.isPublic = isPublic;
    }

    public Tattoo(Long id, String description, String path, boolean isPublic, boolean isDemo) {
        this.id = id;
        this.description = description;
        this.path = path;
        this.isPublic = isPublic;
        this.isDemo = isDemo;
    }

    // ----------------------------------------------------------------------- GETTERS

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getPath() {
        return path;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isDemo() {
        return isDemo;
    }

    // ----------------------------------------------------------------------- SETTERS

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public void setDemo(boolean demo) {
        isDemo = demo;
    }

    // ----------------------------------------------------------------------- OTHERS
    @Override
    public String toString() {
        return "Tattoo[" + ", id='" + id + ", path='" + path+ ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tattoo)) return false;
        Tattoo that = (Tattoo) o;
//        return getId().equals(that.getId()) &&
//                getPath().equals(that.getPath());
        return getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPath());
    }

     public TattooDTO toDTO(){
        TattooDTO tattooDTO = new TattooDTO();
        tattooDTO.setId(id);
        tattooDTO.setDescription(description);
        tattooDTO.setPath(path);
        tattooDTO.setPublic(isPublic);

        return tattooDTO;
     }



}
