package com.epam.esm.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Objects;

public class TagDTO extends AbstractDTO {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9\\s]$")
    private String name;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDTO = (TagDTO) o;
        return name.equals(tagDTO.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TagDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
