package com.epam.esm.web.controller;

import com.epam.esm.web.exception.WebError;
import com.epam.esm.web.exception.WebErrorCode;
import com.epam.esm.web.exception.ControllerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.epam.esm.web.exception.ErrorMessage.FAILED_TO_APPLY_PATCH;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ControllerException.class})
    protected ResponseEntity<Object> handleConflict(ControllerException ex, WebRequest request) {
        return handleExceptionInternal(ex, new WebError(ex), new HttpHeaders(), ex.getErrorCode().getStatus(), request);
    }

    @ExceptionHandler(value = {JsonPatchException.class, JsonProcessingException.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        return handleExceptionInternal(
                ex, new WebError(FAILED_TO_APPLY_PATCH, WebErrorCode.SERVER_ERROR),
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request
        );
    }
}
