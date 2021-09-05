package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.exception.ServiceErrorCode;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.util.CertificateFilter;
import com.epam.esm.web.exception.ControllerError;
import com.epam.esm.web.exception.ControllerErrorCode;
import com.epam.esm.web.exception.ResourceNotFoundException;
import com.epam.esm.web.validation.CertificateValidator;
import com.epam.esm.web.validation.ConstraintViolation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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
import java.util.Set;

import static com.epam.esm.web.exception.ErrorMessage.FAILED_TO_APPLY_PATCH;
import static com.epam.esm.web.exception.ErrorMessage.RELATIONSHIP_EXISTS;
import static com.epam.esm.web.exception.ErrorMessage.RELATIONSHIP_NOT_FOUND;
import static com.epam.esm.web.exception.ErrorMessage.RESOURCE_NOT_FOUND;
import static com.epam.esm.web.exception.ErrorMessage.UNEXPECTED_ERROR;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
    private final CertificateService certificateService;
    private final TagService tagService;
    private final CertificateValidator certificateValidator;
    private final ObjectMapper objectMapper;


    @Autowired
    public CertificateController(CertificateService certificateService, TagService tagService, CertificateValidator certificateValidator, ObjectMapper objectMapper) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.certificateValidator = certificateValidator;
        this.objectMapper = objectMapper;
    }

    /**
     * Retrieves all gift certificates, that match given certificateFilter.
     *
     * @param certificateFilter the filter to be applied.
     * @return list of certificates, if service call was successful.<br/>
     * {@link ControllerError} if certificateFilter had illegal sort properties, or if unexpected error occurred.
     */
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

    /**
     * Creates new certificate from given CertificateDTO.
     *
     * @param certificate certificate to be created.
     * @return created certificate, if certificate is valid and service call was successful.<br/>
     * {@link ControllerError}, if given certificate failed validation process.
     */
    @PostMapping(value = "/certificates")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createCertificate(@RequestBody CertificateDTO certificate) {
        Set<ConstraintViolation> violations = certificateValidator.validateCertificate(certificate);
        if (!violations.isEmpty()) {
            return new ResponseEntity<>(
                    new ControllerError(violations, ControllerErrorCode.CERTIFICATE_VALIDATION_FAILURE),
                    HttpStatus.FORBIDDEN);
        }
        LocalDate now = LocalDate.now();
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);
        return new ResponseEntity<>(certificateService.create(certificate), HttpStatus.CREATED);
    }

    /**
     * Retrieves certificate with given id.
     *
     * @param certificateId id of desired certificate.
     * @return certificate, if one was found.<br/>
     * {@link ControllerError}, if certificate with given id wasn't found.
     */
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

    /**
     * Deletes certificate with given id.
     *
     * @param certificateId id of desired certificate.
     * @return {@link ControllerError}, if certificate with given id wasn't found.
     */
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

    /**
     * Updates certificate with given id. New values are taken from not null fields of given certificate.
     *
     * @param certificateId id of certificate to be updated
     * @param patch         array of patch methods.
     * @return updated certificate, if service call was successful.<br/>
     * {@link ControllerError}, if given certificate failed validation process, or certificate with given id wasn't found.
     */
    @PatchMapping(path = "/certificates/{certificateId}", consumes = "application/json-patch+json")
    public ResponseEntity<Object> updateCertificate(@PathVariable int certificateId, @RequestBody JsonPatch patch) {
        try {
            CertificateDTO certificateDTO = certificateService.get(certificateId).orElseThrow(
                    () -> new ResourceNotFoundException(
                            String.format(RESOURCE_NOT_FOUND, "id=" + certificateId),
                            ControllerErrorCode.CERTIFICATE_NOT_FOUND
                    ));
            CertificateDTO certificatePatched = applyPatchToCustomer(patch, certificateDTO);
            Set<ConstraintViolation> violations = certificateValidator.validateCertificate(certificatePatched);
            if (!violations.isEmpty()) {
                return new ResponseEntity<>(
                        new ControllerError(violations, ControllerErrorCode.CERTIFICATE_VALIDATION_FAILURE),
                        HttpStatus.FORBIDDEN);
            }
            certificatePatched.setLastUpdateDate(LocalDate.now());
            certificateService.update(certificatePatched);
            return ResponseEntity.ok(certificatePatched);
        } catch (JsonPatchException e) {
            return new ResponseEntity<>(new ControllerError(FAILED_TO_APPLY_PATCH, ControllerErrorCode.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(new ControllerError(e), HttpStatus.NOT_FOUND);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(new ControllerError(FAILED_TO_APPLY_PATCH, ControllerErrorCode.SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CertificateDTO applyPatchToCustomer(JsonPatch patch, CertificateDTO certificateDTO) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.valueToTree(certificateDTO));
        return objectMapper.treeToValue(patched, CertificateDTO.class);
    }

    /**
     * Retrieves list of certificates, that correspond to tag with given id
     *
     * @param tagId id of desired tag
     * @return list of certificates, if service call was successful.<br/>
     * {@link ControllerError}, if tag with given id wasn't found.
     */
    @GetMapping(value = "/tags/{tagId}/certificates")
    public ResponseEntity<Object> getCertificatesByTag(@PathVariable int tagId) {
        List<CertificateDTO> certificates = certificateService.getAll(tagId);
        if (certificates.isEmpty() && !tagService.idExists(tagId)) {
            return new ResponseEntity<>(
                    new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + tagId), ControllerErrorCode.TAG_NOT_FOUND),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    /**
     * Assign tag with given id to certificate with given id.
     *
     * @param tagId         id of desired tag.
     * @param certificateId id of desired certificate.
     * @return {@link ControllerError} if that relationship already exists, if tag or certificate wasn't found,
     * or if unexpected error occurred.
     */
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
            } else if (e.getErrorCode().equals(ServiceErrorCode.BAD_KEY)) {
                StringBuilder msg = new StringBuilder();
                if (!certificateService.idExists(certificateId)) {
                    msg.append("certificateId=").append(certificateId).append(";");
                }
                if (!tagService.idExists(tagId)) {
                    msg.append("tagId=").append(tagId);
                }
                return new ResponseEntity<>(
                        new ControllerError(String.format(RESOURCE_NOT_FOUND, msg), ControllerErrorCode.ENTITY_NOT_FOUND),
                        HttpStatus.NOT_FOUND
                );
            } else {
                return new ResponseEntity<>(
                        new ControllerError(UNEXPECTED_ERROR, ControllerErrorCode.SERVER_ERROR),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Remove tag with given id from certificate with given id.
     *
     * @param tagId         id of desired tag.
     * @param certificateId id of desired certificate.
     * @return {@link ControllerError} if that relationship wasn't found.
     */
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
