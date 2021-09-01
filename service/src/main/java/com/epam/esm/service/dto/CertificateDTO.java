package com.epam.esm.service.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;

public class CertificateDTO extends AbstractDTO {
    @Size(min = 3, max = 50, message = "Certificate name must be {min}-{max} characters long.")
    @NotBlank(message = "Certificate name must not be blank.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]$", message = "Certificate name must be alphanumeric.")
    private String name;

    @Size(min = 10, max = 3000, message = "Certificate description must be {min}-{max} characters long.")
    @NotBlank(message = "Certificate description must not be blank.")
    private String description;

    @Positive(message = "Certificate price must be positive number.")
    private Integer price;

    @Positive(message = "Certificate duration must be positive number.")
    private Integer duration;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate createDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate lastUpdateDate;

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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
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
                '}';
    }

    public class CertificateDTOBuilder {
        private CertificateDTOBuilder() {
        }

        public CertificateDTOBuilder withId(int id) {
            CertificateDTO.this.id = id;
            return this;
        }

        public CertificateDTOBuilder withName(String name) {
            CertificateDTO.this.name = name;
            return this;
        }

        public CertificateDTOBuilder withDescription(String description) {
            CertificateDTO.this.description = description;
            return this;
        }

        public CertificateDTOBuilder withPrice(Integer price) {
            CertificateDTO.this.price = price;
            return this;
        }

        public CertificateDTOBuilder withDuration(Integer duration) {
            CertificateDTO.this.duration = duration;
            return this;
        }

        public CertificateDTOBuilder withCreateDate(LocalDate createDate) {
            CertificateDTO.this.createDate = createDate;
            return this;
        }

        public CertificateDTOBuilder withLastUpdateDate(LocalDate lastUpdateDate) {
            CertificateDTO.this.lastUpdateDate = lastUpdateDate;
            return this;
        }

        public CertificateDTO build() {
            CertificateDTO certificate = new CertificateDTO();
            certificate.id = CertificateDTO.this.id;
            certificate.name = CertificateDTO.this.name;
            certificate.description = CertificateDTO.this.description;
            certificate.duration = CertificateDTO.this.duration;
            certificate.price = CertificateDTO.this.price;
            certificate.createDate = CertificateDTO.this.createDate;
            certificate.lastUpdateDate = CertificateDTO.this.lastUpdateDate;
            return certificate;
        }
    }
}
