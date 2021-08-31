package com.epam.esm.service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class TagDTO extends AbstractDTO {
    @NotBlank(message = "Tag name must not be blank.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]$", message = "Tag name must be alphanumeric.")
    @Size(min = 3, max = 25, message = "{Tag name must be {min}-{max} characters long.")
    private String name;

    public TagDTO(){
    }

    public TagDTO(String name) {
        this.name = name;
    }

    public TagDTO(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return name.equals(tagDTO.name);
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
