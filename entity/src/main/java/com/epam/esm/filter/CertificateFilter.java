package com.epam.esm.filter;

public class CertificateFilter {
    private String namePart;
    private String descriptionPart;
    private String tagName;
    private Sort sort;

    public CertificateFilter() {
    }

    public static CertificateFilterBuilder newBuilder() {
        return new CertificateFilter().new CertificateFilterBuilder();
    }

    public String getNamePart() {
        return namePart;
    }

    public void setNamePart(String namePart) {
        this.namePart = namePart;
    }

    public String getDescriptionPart() {
        return descriptionPart;
    }

    public void setDescriptionPart(String descriptionPart) {
        this.descriptionPart = descriptionPart;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public class CertificateFilterBuilder {
        private CertificateFilterBuilder() {
        }

        public CertificateFilterBuilder withNamePart(String namePart) {
            CertificateFilter.this.namePart = namePart;
            return this;
        }

        public CertificateFilterBuilder withDescriptionPart(String descriptionPart) {
            CertificateFilter.this.descriptionPart = descriptionPart;
            return this;
        }

        public CertificateFilterBuilder withTagName(String tagName) {
            CertificateFilter.this.tagName = tagName;
            return this;
        }

        public CertificateFilterBuilder withSort(Sort sort) {
            CertificateFilter.this.sort = sort;
            return this;
        }

        public CertificateFilter build() {
            CertificateFilter certificateFilter = new CertificateFilter();
            certificateFilter.namePart = CertificateFilter.this.namePart;
            certificateFilter.descriptionPart = CertificateFilter.this.descriptionPart;
            certificateFilter.tagName = CertificateFilter.this.tagName;
            certificateFilter.sort = CertificateFilter.this.sort;
            return certificateFilter;
        }
    }
}
