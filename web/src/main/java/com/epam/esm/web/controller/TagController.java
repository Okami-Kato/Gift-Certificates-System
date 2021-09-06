package com.epam.esm.web.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ServiceError;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.exception.WebErrorCode;
import com.epam.esm.web.exception.ControllerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static com.epam.esm.web.exception.ErrorMessage.RESOURCE_NOT_FOUND;
import static com.epam.esm.web.exception.ErrorMessage.UNEXPECTED_ERROR;

/**
 * REST controller, that handles all HTTP requests related to tags
 */
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private final TagService tagService;
    private final CertificateService certificateService;

    @Autowired
    public TagController(TagService tagService, CertificateService certificateService) {
        this.tagService = tagService;
        this.certificateService = certificateService;
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
     * @return created tag.
     * @throws ControllerException if given tag isn't valid, if tag with the same name already exists,
     *                             or if unexpected error occurred.
     */
    @PostMapping(value = "/tags")
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO createTag(@RequestBody TagDTO tag) {
        try {
            return tagService.create(tag);
        } catch (ServiceException e) {
            if (e.getError().equals(ServiceError.DUPLICATE_TAG_NAME)) {
                throw new ControllerException(e.getMessage(), WebErrorCode.DUPLICATE_TAG_NAME);
            } else if (e.getError().equals(ServiceError.TAG_VALIDATION_FAILURE)) {
                throw new ControllerException(e.getMessage(), WebErrorCode.TAG_VALIDATION_FAILURE, e.getArgs());
            } else {
                throw new ControllerException(UNEXPECTED_ERROR, WebErrorCode.SERVER_ERROR);
            }
        }
    }

    /**
     * Retrieves tag with given id.
     *
     * @param id id of desired tag.
     * @return found tag.
     * @throws ControllerException if tag wasn't found.
     */
    @GetMapping(value = "/tags/{id}")
    public TagDTO getTag(@PathVariable int id) {
        Optional<TagDTO> tag = tagService.get(id);
        return tag.orElseThrow(() -> new ControllerException(
                String.format(RESOURCE_NOT_FOUND, "id=" + id),
                WebErrorCode.TAG_NOT_FOUND
        ));
    }

    /**
     * Deletes tag with given id.
     *
     * @param id id of tag to be deleted
     * @throws ControllerException if tag wasn't found.
     */
    @DeleteMapping(value = "/tags/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTag(@PathVariable int id) {
        if (!tagService.delete(id)) {
            throw new ControllerException(
                    String.format(RESOURCE_NOT_FOUND, "id=" + id),
                    WebErrorCode.TAG_NOT_FOUND
            );
        }
    }

    /**
     * Retrieves tags, which are assigned to certificate with given id.
     *
     * @param id id of desired certificate.
     * @return list of tags.
     * @throws ControllerException if certificate wasn't found
     */
    @GetMapping(value = "/certificates/{id}/tags")
    public List<TagDTO> getCertificateTags(@PathVariable int id) {
        final List<TagDTO> tags = tagService.getAllByCertificateId(id);
        if (tags.isEmpty() && !certificateService.idExists(id)) {
            throw new ControllerException(
                    String.format(RESOURCE_NOT_FOUND, "id=" + id), WebErrorCode.CERTIFICATE_NOT_FOUND
            );
        }
        return tags;
    }
}
