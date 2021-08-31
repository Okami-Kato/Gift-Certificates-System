package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.web.exception.ControllerError;
import com.epam.esm.web.exception.ErrorCode;
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

@RestController
@RequestMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
    private final CertificateService certificateService;
    private final String RESOURCE_NOT_FOUND = "Resource not found (%s)";


    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "")
    public List<CertificateDTO> getAllCertificates() {
        return certificateService.getAll();
    }

    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDTO createCertificate(@Valid @RequestBody CertificateDTO certificate) {
        LocalDate now = LocalDate.now();
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);
        return certificateService.create(certificate);
    }

    @GetMapping(value = "/{certificateId}")
    public ResponseEntity<Object> getCertificate(@PathVariable int certificateId) {
        Optional<CertificateDTO> certificate = certificateService.get(certificateId);
        return certificate.<ResponseEntity<Object>>map(certificateDTO -> new ResponseEntity<>(certificateDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + certificateId), ErrorCode.CERTIFICATE_NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{certificateId}")
    public ResponseEntity<Object> deleteCertificate(@PathVariable int certificateId) {
        if (certificateService.delete(certificateId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + certificateId), ErrorCode.CERTIFICATE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{certificateId}")
    public ResponseEntity<Object> updateCertificate(@PathVariable int certificateId, @Valid @RequestBody CertificateDTO certificate) {
        certificate.setLastUpdateDate(LocalDate.now());
        certificate.setId(certificateId);
        if (certificateService.update(certificate)) {
            return new ResponseEntity<>(certificate, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + certificateId), ErrorCode.CERTIFICATE_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
}
