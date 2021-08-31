package com.epam.esm.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Certificate extends Entity {
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    private LocalDate createDate;
    private LocalDate lastUpdateDate;

    private Certificate() {
    }

    public static CertificateBuilder newBuilder() {
        return new Certificate().new CertificateBuilder();
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

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getDuration() {
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

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration, createDate, lastUpdateDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return Objects.equals(price, that.price) && Objects.equals(duration, that.duration) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", lastUpdateDate=" + lastUpdateDate +
                ", id=" + id +
                '}';
    }

    public class CertificateBuilder {
        private CertificateBuilder() {
        }

        public CertificateBuilder setId(int id) {
            Certificate.this.setId(id);
            return this;
        }

        public CertificateBuilder setName(String name) {
            Certificate.this.setName(name);
            return this;
        }

        public CertificateBuilder setDescription(String description) {
            Certificate.this.setDescription(description);
            return this;
        }

        public CertificateBuilder setPrice(int price) {
            Certificate.this.setPrice(price);
            return this;
        }

        public CertificateBuilder setDuration(int duration) {
            Certificate.this.setDuration(duration);
            return this;
        }

        public CertificateBuilder setCreateDate(LocalDate createDate) {
            Certificate.this.setCreateDate(createDate);
            return this;
        }

        public CertificateBuilder setLastUpdateDate(LocalDate updateDate) {
            Certificate.this.setLastUpdateDate(updateDate);
            return this;
        }

        public Certificate build() {
            Certificate certificate = new Certificate();
            certificate.id = Certificate.this.id;
            certificate.name = Certificate.this.name;
            certificate.description = Certificate.this.description;
            certificate.duration = Certificate.this.duration;
            certificate.price = Certificate.this.price;
            certificate.createDate = Certificate.this.createDate;
            certificate.lastUpdateDate = Certificate.this.lastUpdateDate;
            return certificate;
        }
    }
}
