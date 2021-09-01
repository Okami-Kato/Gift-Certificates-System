package com.epam.esm.web.controller;

import com.epam.esm.filter.CertificateFilter;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.exception.ServiceErrorCode;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.exception.ControllerError;
import com.epam.esm.web.exception.ControllerErrorCode;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.web.exception.ErrorMessage.RELATIONSHIP_EXISTS;
import static com.epam.esm.web.exception.ErrorMessage.RELATIONSHIP_NOT_FOUND;
import static com.epam.esm.web.exception.ErrorMessage.RESOURCE_NOT_FOUND;
import static com.epam.esm.web.exception.ErrorMessage.UNEXPECTED_ERROR;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "/certificates")
    public ResponseEntity<Object> getAllCertificates(@RequestBody(required = false) CertificateFilter certificateFilter) {
        if (certificateFilter != null) {
            try {
                return new ResponseEntity<>(certificateService.getAll(certificateFilter), HttpStatus.OK);
            } catch (ServiceException e) {
                if (e.getErrorCode().equals(ServiceErrorCode.BAD_SORT_PROPERTY)) {
                    return new ResponseEntity<>(
                            new ControllerError(e.getMessage(), ControllerErrorCode.BAD_SORT_PROPERTY),
                            HttpStatus.NOT_FOUND
                    );
                } else {
                    return new ResponseEntity<>(
                            new ControllerError(
                                    UNEXPECTED_ERROR, ControllerErrorCode.SERVER_ERROR),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            return new ResponseEntity<>(certificateService.getAll(), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/certificates")
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDTO createCertificate(@Valid @RequestBody CertificateDTO certificate) {
        LocalDate now = LocalDate.now();
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);
        return certificateService.create(certificate);
    }

    @GetMapping(value = "/certificates/{certificateId}")
    public ResponseEntity<Object> getCertificate(@PathVariable int certificateId) {
        Optional<CertificateDTO> certificate = certificateService.get(certificateId);
        return certificate.<ResponseEntity<Object>>map(certificateDTO -> new ResponseEntity<>(certificateDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        new ControllerError(
                                String.format(RESOURCE_NOT_FOUND, "id=" + certificateId),
                                ControllerErrorCode.CERTIFICATE_NOT_FOUND),
                        HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/certificates/{certificateId}")
    public ResponseEntity<Object> deleteCertificate(@PathVariable int certificateId) {
        if (certificateService.delete(certificateId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ControllerError(
                            String.format(RESOURCE_NOT_FOUND, "id=" + certificateId),
                            ControllerErrorCode.CERTIFICATE_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(value = "/certificates/{certificateId}")
    public ResponseEntity<Object> updateCertificate(@PathVariable int certificateId, @Valid @RequestBody CertificateDTO certificate) {
        certificate.setLastUpdateDate(LocalDate.now());
        certificate.setId(certificateId);
        if (certificateService.update(certificate)) {
            return getCertificate(certificateId);
        } else {
            return new ResponseEntity<>(
                    new ControllerError(
                            String.format(RESOURCE_NOT_FOUND, "id=" + certificateId),
                            ControllerErrorCode.CERTIFICATE_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/tags/{tagId}/certificates")
    public List<CertificateDTO> getCertificatesByTag(@PathVariable int tagId) {
        return certificateService.getAll(tagId);
    }

    @PutMapping(value = {"/tags/{tagId}/certificates/{certificateId}", "certificates/{certificateId}/tags/{tagId}"})
    public ResponseEntity<Object> assignCertificateToTag(@PathVariable int tagId, @PathVariable int certificateId) {
        try {
            certificateService.addTag(certificateId, tagId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ServiceException e) {
            if (e.getErrorCode().equals(ServiceErrorCode.DUPLICATE_CERTIFICATE_TAG)) {
                return new ResponseEntity<>(
                        new ControllerError(
                                String.format(RELATIONSHIP_EXISTS, "certificateId=" + certificateId + ", tagId=" + tagId),
                                ControllerErrorCode.DUPLICATE_CERTIFICATE_TAG),
                        HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(
                        new ControllerError(
                                UNEXPECTED_ERROR, ControllerErrorCode.SERVER_ERROR),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @DeleteMapping(value = {"/tags/{tagId}/certificates/{certificateId}", "certificates/{certificateId}/tags/{tagId}"})
    public ResponseEntity<Object> removeCertificateFromTag(@PathVariable int tagId, @PathVariable int certificateId) {
        if (certificateService.removeTag(certificateId, tagId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ControllerError(String.format(RELATIONSHIP_NOT_FOUND, "certificateId=" + certificateId + ", tagId=" + tagId),
                            ControllerErrorCode.TAG_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }
}
