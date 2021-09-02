package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ServiceErrorCode;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.exception.ControllerError;
import com.epam.esm.web.exception.ControllerErrorCode;
import com.epam.esm.web.validation.ConstraintViolation;
import com.epam.esm.web.validation.TagValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.web.exception.ErrorMessage.RESOURCE_EXISTS;
import static com.epam.esm.web.exception.ErrorMessage.RESOURCE_NOT_FOUND;
import static com.epam.esm.web.exception.ErrorMessage.UNEXPECTED_ERROR;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private final TagService tagService;
    private final CertificateService certificateService;
    private final TagValidator tagValidator;

    @Autowired
    public TagController(TagService tagService, CertificateService certificateService, TagValidator tagValidator) {
        this.tagService = tagService;
        this.certificateService = certificateService;
        this.tagValidator = tagValidator;
    }

    /**
     * Retrieves all tags.
     *
     * @return list of tags.
     */
    @GetMapping(value = "/tags")
    public List<TagDTO> getAllTags() {
        return tagService.getAll();
    }

    /**
     * Creates new tag from give TagDTO.
     *
     * @param tag tag to be created.
     * @return created tag, if tag is valid and service call was successful.<br/>
     * {@link ControllerError}, if given tag failed validation process.
     */
    @PostMapping(value = "/tags")
    public ResponseEntity<Object> createTag(@RequestBody TagDTO tag) {
        Set<ConstraintViolation> violations = tagValidator.validateTag(tag, true);
        if (!violations.isEmpty()) {
            return new ResponseEntity<>(violations, HttpStatus.FORBIDDEN);
        }

        try {
            return new ResponseEntity<>(tagService.create(tag), HttpStatus.CREATED);
        } catch (ServiceException e) {
            if (e.getErrorCode().equals(ServiceErrorCode.DUPLICATE_TAG_NAME)) {
                return new ResponseEntity<>(
                        new ControllerError(
                                String.format(RESOURCE_EXISTS, "name=" + tag.getName()),
                                ControllerErrorCode.DUPLICATE_TAG_NAME),
                        HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(
                        new ControllerError(
                                UNEXPECTED_ERROR, ControllerErrorCode.SERVER_ERROR),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    /**
     * Retrieves tag with given id.
     *
     * @param tagId id of desired tag.
     * @return tag, one was found.<br/>
     * {@link ControllerError}, if tag with given id wasn't found.
     */
    @GetMapping(value = "/tags/{tagId}")
    public ResponseEntity<Object> getTag(@PathVariable int tagId) {
        Optional<TagDTO> tag = tagService.get(tagId);
        return tag.<ResponseEntity<Object>>map(tagDTO -> new ResponseEntity<>(tagDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(
                        new ControllerError(
                                String.format(RESOURCE_NOT_FOUND, "id=" + tagId),
                                ControllerErrorCode.TAG_NOT_FOUND),
                        HttpStatus.NOT_FOUND));
    }

    /**
     * Deletes tag with given id.
     *
     * @param tagId id of tag to be deleted
     * @return {@link ControllerError}, if tag with given id wasn't found.
     */
    @DeleteMapping(value = "/tags/{tagId}")
    public ResponseEntity<Object> deleteTag(@PathVariable int tagId) {
        if (tagService.delete(tagId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + tagId),
                            ControllerErrorCode.TAG_NOT_FOUND),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves tags, which are assigned to certificate with given id.
     *
     * @param certificateId id of desired certificate.
     * @return list of tags.
     */
    @GetMapping(value = "/certificates/{certificateId}/tags")
    public ResponseEntity<Object> getCertificateTags(@PathVariable int certificateId) {
        final List<TagDTO> tags = tagService.getAllByCertificateId(certificateId);
        if (tags.isEmpty() && !certificateService.idExists(certificateId)) {
            return new ResponseEntity<>(
                    new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + certificateId), ControllerErrorCode.CERTIFICATE_NOT_FOUND),
                    HttpStatus.NOT_FOUND
            );
        }
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }
}
