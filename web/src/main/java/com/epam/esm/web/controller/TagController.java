package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ServiceErrorCode;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.web.exception.ControllerError;
import com.epam.esm.web.exception.ControllerErrorCode;
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

import static com.epam.esm.web.exception.ErrorMessage.RESOURCE_EXISTS;
import static com.epam.esm.web.exception.ErrorMessage.RESOURCE_NOT_FOUND;
import static com.epam.esm.web.exception.ErrorMessage.UNEXPECTED_ERROR;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/tags")
    public List<TagDTO> getAllTags() {
        return tagService.getAll();
    }

    @PostMapping(value = "/tags")
    public ResponseEntity<Object> createTag(@RequestBody TagDTO tag) {
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

    @GetMapping(value = "/certificates/{certificateId}/tags")
    public List<TagDTO> getCertificateTags(@PathVariable int certificateId) {
        return tagService.getAllByCertificateId(certificateId);
    }
}
