package com.epam.esm.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Certificate extends Entity {
    private String name;
    private String description;
    private int price;
    private int duration;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
        return price == that.price && duration == that.duration && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(createDate, that.createDate) && Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, price, duration, createDate, lastUpdateDate);
    }

    @Override
    public String toString() {
        return "GiftCertificate{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", duration=" + duration +
                ", createDate=" + createDate +
                ", updateDate=" + lastUpdateDate +
                '}';
    }

    public class CertificateBuilder {
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
            certificate.setName(name);
            certificate.setDescription(description);
            certificate.setPrice(price);
            certificate.setDuration(duration);
            certificate.setCreateDate(createDate);
            certificate.setLastUpdateDate(lastUpdateDate);
            return certificate;
        }
    }
}