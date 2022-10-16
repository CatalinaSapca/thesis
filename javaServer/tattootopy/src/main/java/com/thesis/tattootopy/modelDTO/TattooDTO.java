package com.thesis.tattootopy.modelDTO;

import com.thesis.tattootopy.model.Tattoo;

import java.io.Serializable;
import java.util.Objects;

public class TattooDTO implements Serializable {

    private Long id;
    private String description;
    private String path;
    private boolean isPublic;

    // ----------------------------------------------------------------------- CONSTRUCTORS
    public TattooDTO() {
    }

    public TattooDTO(Long id, String description, String path) {
        this.id = id;
        this.description = description;
        this.path = path;
    }

    public TattooDTO(Long id, String description, String path, boolean isPublic) {
        this.id = id;
        this.description = description;
        this.path = path;
        this.isPublic = isPublic;
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

    // ----------------------------------------------------------------------- OTHERS

    @Override
    public String toString() {
        return "TattooDTO{" + "id=" + id + ", description='" + description + ", path='" + path  + ']';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TattooDTO)) return false;
        TattooDTO that = (TattooDTO) o;
        return getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDescription());
    }

    public Tattoo toTattoo(){
        Tattoo tattoo = new Tattoo();
        tattoo.setId(id);
        tattoo.setDescription(description);
        tattoo.setPath(path);
        tattoo.setPublic(isPublic);

        return tattoo;
    }


}

