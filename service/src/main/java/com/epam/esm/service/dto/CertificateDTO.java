package com.epam.esm.service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class CertificateDTO extends AbstractDTO {
    private List<TagDTO> tagList = new LinkedList<>();
    @NotEmpty
    private String name;
    private String description;
    @Positive
    private int price;
    @Positive
    private int duration;
    @NotNull
    private LocalDate createDate;
    @NotNull
    private LocalDate lastUpdateDate;

    private CertificateDTO() {
    }

    public static CertificateDTO.CertificateDTOBuilder newBuilder() {
        return new CertificateDTO().new CertificateDTOBuilder();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }

    public LocalDate getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDate lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public List<TagDTO> getTagList() {
        return tagList;
    }

    public void setTagList(List<TagDTO> tagList) {
        this.tagList = tagList;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration, createDate, lastUpdateDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CertificateDTO that = (CertificateDTO) o;
        return price == that.price && duration == that.duration && name.equals(that.name) && description.equals(that.description) && createDate.equals(that.createDate) && lastUpdateDate.equals(that.lastUpdateDate);
    }

    @Override
    public String toString() {
        return "CertificateDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", tagList=" + tagList +
                '}';
    }

    public class CertificateDTOBuilder {
        private CertificateDTOBuilder() {
        }

        public CertificateDTOBuilder setId(int id) {
            CertificateDTO.this.setId(id);
            return this;
        }

        public CertificateDTOBuilder setName(String name) {
            CertificateDTO.this.setName(name);
            return this;
        }

        public CertificateDTOBuilder setDescription(String description) {
            CertificateDTO.this.setDescription(description);
            return this;
        }

        public CertificateDTOBuilder setPrice(int price) {
            CertificateDTO.this.setPrice(price);
            return this;
        }

        public CertificateDTOBuilder setDuration(int duration) {
            CertificateDTO.this.setDuration(duration);
            return this;
        }

        public CertificateDTOBuilder setCreateDate(LocalDate createDate) {
            CertificateDTO.this.setCreateDate(createDate);
            return this;
        }

        public CertificateDTOBuilder setLastUpdateDate(LocalDate updateDate) {
            CertificateDTO.this.setLastUpdateDate(updateDate);
            return this;
        }

        public CertificateDTOBuilder setTags(TagDTO... tags) {
            CertificateDTO.this.setTagList(Arrays.asList(tags));
            return this;
        }

        public CertificateDTO build() {
            CertificateDTO certificate = new CertificateDTO();
            certificate.setId(id);
            certificate.setName(name);
            certificate.setDescription(description);
            certificate.setPrice(price);
            certificate.setDuration(duration);
            certificate.setCreateDate(createDate);
            certificate.setLastUpdateDate(lastUpdateDate);
            certificate.setTagList(tagList);
            return certificate;
        }
    }
}
