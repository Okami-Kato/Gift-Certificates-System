package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.CertificateDTO;
import com.epam.esm.service.exception.ServiceError;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.util.CertificateFilter;
import com.epam.esm.web.exception.ControllerErrorCode;
import com.epam.esm.web.exception.ControllerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

/**
 * REST controller, that handles all HTTP requests related to certificates
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {
    private final CertificateService certificateService;
    private final TagService tagService;
    private final ObjectMapper objectMapper;


    @Autowired
    public CertificateController(CertificateService certificateService, TagService tagService, ObjectMapper objectMapper) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
    }

    /**
     * Retrieves all gift certificates, that match given certificateFilter.
     *
     * @param certificateFilter the filter to be applied.
     * @return list of found certificates.
     * @throws ControllerException if given filter's sort properties contain non-existent sort properties,
     *                             or if unexpected error occurred.
     */
    @GetMapping(value = "/certificates")
    public List<CertificateDTO> getAllCertificates(@RequestBody(required = false) CertificateFilter certificateFilter) {
        if (certificateFilter != null) {
            try {
                return certificateService.getAll(certificateFilter);
            } catch (ServiceException e) {
                if (e.getError().equals(ServiceError.BAD_SORT_PROPERTY)) {
                    throw new ControllerException(e.getMessage(), ControllerErrorCode.BAD_SORT_PROPERTY);
                } else {
                    throw new ControllerException(e.getMessage(), ControllerErrorCode.SERVER_ERROR);
                }
            }
        } else {
            return certificateService.getAll();
        }
    }

    /**
     * Creates new certificate from given CertificateDTO.
     *
     * @param certificate certificate to be created.
     * @return created certificate.
     * @throws ControllerException if given certificate isn't valid.
     */
    @PostMapping(value = "/certificates")
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDTO createCertificate(@RequestBody CertificateDTO certificate) {
        LocalDate now = LocalDate.now();
        certificate.setCreateDate(now);
        certificate.setLastUpdateDate(now);
        try {
            return certificateService.create(certificate);
        } catch (ServiceException e) {
            if (e.getError().equals(ServiceError.CERTIFICATE_VALIDATION_FAILURE)) {
                throw new ControllerException(e.getMessage(),
                        ControllerErrorCode.CERTIFICATE_VALIDATION_FAILURE, e.getArgs());
            } else {
                throw new ControllerException(UNEXPECTED_ERROR, ControllerErrorCode.SERVER_ERROR);
            }
        }
    }

    /**
     * Retrieves certificate with given id.
     *
     * @param id id of desired certificate.
     * @return found certificate.
     * @throws ControllerException if certificate wasn't found.
     */
    @GetMapping(value = "/certificates/{id}")
    public CertificateDTO getCertificate(@PathVariable int id) {
        Optional<CertificateDTO> certificate = certificateService.get(id);
        return certificate.orElseThrow(() -> new ControllerException(
                String.format(RESOURCE_NOT_FOUND, "id=" + id),
                ControllerErrorCode.CERTIFICATE_NOT_FOUND
        ));
    }

    /**
     * Deletes certificate with given id.
     *
     * @param id id of desired certificate.
     * @throws ControllerException if certificate wasn't found.
     */
    @DeleteMapping(value = "/certificates/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCertificate(@PathVariable int id) {
        if (!certificateService.delete(id)) {
            throw new ControllerException(
                    String.format(RESOURCE_NOT_FOUND, "id=" + id),
                    ControllerErrorCode.CERTIFICATE_NOT_FOUND
            );
        }
    }

    /**
     * Updates certificate with given id. New values are taken from not null fields of given certificate.
     *
     * @param id    id of certificate to be updated
     * @param patch array of patch methods.
     * @return updated certificate.
     * @throws ControllerException     if certificate wasn't found, if updated certificate isn't valid.
     * @throws JsonPatchException      if failed to apply patch.
     * @throws JsonProcessingException if structural conversion failed.
     */
    @PatchMapping(path = "/certificates/{id}", consumes = "application/json-patch+json")
    public CertificateDTO updateCertificate(@PathVariable int id, @RequestBody JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        CertificateDTO certificateDTO = certificateService.get(id).orElseThrow(
                () -> new ControllerException(
                        String.format(RESOURCE_NOT_FOUND, "id=" + id),
                        ControllerErrorCode.CERTIFICATE_NOT_FOUND
                ));
        CertificateDTO certificatePatched = applyPatchToCustomer(patch, certificateDTO);
        certificatePatched.setLastUpdateDate(LocalDate.now());
        try {
            certificateService.update(certificatePatched);
        } catch (ServiceException e) {
            if (e.getError().equals(ServiceError.CERTIFICATE_VALIDATION_FAILURE)) {
                throw new ControllerException(e.getMessage(),
                        ControllerErrorCode.CERTIFICATE_VALIDATION_FAILURE, e.getArgs());
            } else {
                throw new ControllerException(UNEXPECTED_ERROR, ControllerErrorCode.SERVER_ERROR);
            }
        }
        return certificatePatched;
    }

    private CertificateDTO applyPatchToCustomer(JsonPatch patch, CertificateDTO certificateDTO) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.valueToTree(certificateDTO));
        return objectMapper.treeToValue(patched, CertificateDTO.class);
    }

    /**
     * Retrieves list of certificates, that correspond to tag with given id.
     *
     * @param id id of desired tag.
     * @return list of certificates.
     * @throws ControllerException if tag wasn't found.
     */
    @GetMapping(value = "/tags/{id}/certificates")
    public List<CertificateDTO> getCertificatesByTag(@PathVariable int id) {
        List<CertificateDTO> certificates = certificateService.getAll(id);
        if (certificates.isEmpty() && !tagService.idExists(id)) {
            throw new ControllerException(
                    String.format(RESOURCE_NOT_FOUND, "id=" + id), ControllerErrorCode.TAG_NOT_FOUND
            );
        }
        return certificates;
    }

    /**
     * Assign tag with given id to certificate with given id.
     *
     * @param tagId         id of desired tag.
     * @param certificateId id of desired certificate.
     * @throws ControllerException if relationship already exists, if tag or certificate wasn't found,
     *                             or if unexpected error occurred.
     */
    @PutMapping(value = {"/tags/{tagId}/certificates/{certificateId}", "certificates/{certificateId}/tags/{tagId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignCertificateToTag(@PathVariable int tagId, @PathVariable int certificateId) {
        try {
            certificateService.addTag(certificateId, tagId);
        } catch (ServiceException e) {
            if (e.getError().equals(ServiceError.DUPLICATE_CERTIFICATE_TAG)) {
                throw new ControllerException(
                        String.format(RELATIONSHIP_EXISTS, "certificateId=" + certificateId + ", tagId=" + tagId),
                        ControllerErrorCode.DUPLICATE_CERTIFICATE_TAG
                );
            } else if (e.getError().equals(ServiceError.BAD_KEY)) {
                StringBuilder msg = new StringBuilder();
                if (!certificateService.idExists(certificateId)) {
                    msg.append("certificateId=").append(certificateId).append(";");
                }
                if (!tagService.idExists(tagId)) {
                    msg.append("tagId=").append(tagId);
                }
                throw new ControllerException(String.format(RESOURCE_NOT_FOUND, msg), ControllerErrorCode.ENTITY_NOT_FOUND);
            } else {
                throw new ControllerException(UNEXPECTED_ERROR, ControllerErrorCode.SERVER_ERROR);
            }
        }
    }

    /**
     * Remove tag with given id from certificate with given id.
     *
     * @param tagId         id of desired tag.
     * @param certificateId id of desired certificate.
     * @throws ControllerException if relationship wasn't found.
     */
    @DeleteMapping(value = {"/tags/{tagId}/certificates/{certificateId}", "certificates/{certificateId}/tags/{tagId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCertificateFromTag(@PathVariable int tagId, @PathVariable int certificateId) {
        if (!certificateService.removeTag(certificateId, tagId)) {
            throw new ControllerException(
                    String.format(RELATIONSHIP_NOT_FOUND, "certificateId=" + certificateId + ", tagId=" + tagId),
                    ControllerErrorCode.TAG_NOT_FOUND
            );
        }
    }
}
