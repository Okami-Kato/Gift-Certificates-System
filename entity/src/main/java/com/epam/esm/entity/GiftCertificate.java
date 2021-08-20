package com.epam.esm.entity;

import java.time.LocalDate;

public class GiftCertificate extends Entity {
    private String name;
    private String description;
    private int price;
    private int duration;
    private LocalDate createDate;
    private LocalDate updateDate;

    private GiftCertificate() {
    }

    public static GiftCertificateBuilder newBuilder() {
        return new GiftCertificate().new GiftCertificateBuilder();
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

    public LocalDate getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDate updateDate) {
        this.updateDate = updateDate;
    }

    private class GiftCertificateBuilder {
        public GiftCertificateBuilder setName(String name) {
            GiftCertificate.this.setName(name);
            return this;
        }

        public GiftCertificateBuilder setDescription(String description) {
            GiftCertificate.this.setDescription(description);
            return this;
        }

        public GiftCertificateBuilder setPrice(int price) {
            GiftCertificate.this.setPrice(price);
            return this;
        }

        public GiftCertificateBuilder setDuration(int duration) {
            GiftCertificate.this.setDuration(duration);
            return this;
        }

        public GiftCertificateBuilder setCreateDate(LocalDate createDate) {
            GiftCertificate.this.setCreateDate(createDate);
            return this;
        }

        public GiftCertificateBuilder setUpdateDate(LocalDate updateDate) {
            GiftCertificate.this.setUpdateDate(updateDate);
            return this;
        }

        public GiftCertificate build() {
            GiftCertificate certificate = new GiftCertificate();
            certificate.setName(name);
            certificate.setDescription(description);
            certificate.setPrice(price);
            certificate.setDuration(duration);
            certificate.setCreateDate(createDate);
            certificate.setUpdateDate(updateDate);
            return certificate;
        }
    }
}
