package com.epam.esm.web.controller;

import com.epam.esm.service.TagService;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.web.exception.ControllerError;
import com.epam.esm.web.exception.ErrorCode;
import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {
    private final TagService tagService;
    private final String RESOURCE_NOT_FOUND = "Resource not found (%s)";
    private final String RESOURCE_EXISTS = "Resource already exists (%s)";

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "")
    public List<TagDTO> getAllTags() {
        return tagService.getAll();
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> createCertificate(@Valid @RequestBody TagDTO tag) {
        if (!tagService.nameExists(tag.getName())){
            return new ResponseEntity<>(tagService.create(tag), HttpStatus.CREATED);
        } else{
            return new ResponseEntity<>(new ControllerError(String.format(RESOURCE_EXISTS, "name=" + tag.getName()), ErrorCode.TAG_EXISTS), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/{tagId}")
    public ResponseEntity<Object> getCertificate(@PathVariable int tagId) {
        Optional<TagDTO> tag = tagService.get(tagId);
        return tag.<ResponseEntity<Object>>map(tagDTO -> new ResponseEntity<>(tagDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + tagId), ErrorCode.TAG_NOT_FOUND), HttpStatus.NOT_FOUND));
    }

    @DeleteMapping(value = "/{tagId}")
    public ResponseEntity<Object> deleteCertificate(@PathVariable int tagId) {
        if (tagService.delete(tagId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ControllerError(String.format(RESOURCE_NOT_FOUND, "id=" + tagId), ErrorCode.TAG_NOT_FOUND), HttpStatus.NOT_FOUND);
        }
    }
}
