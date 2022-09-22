package com.pharmacy.service.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ToString
@Slf4j
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final transient Object fieldValue;
    private final String message;

    public ResourceException(String resourceName, String fieldName, Object fieldValue, ErrorType errorType, Exception e) {
        super(resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "! " + e.getMessage());
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.message = resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "! " + e.getMessage();
        log.info(e.toString());
    }

    public ResourceException(String resourceName, String fieldName, Object fieldValue, ErrorType errorType, String message) {
        super(resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "! " + message);
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.message = resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "! " + message;
    }

    public ResourceException(String resourceName, String fieldName, Object fieldValue, ErrorType errorType) {
        super(resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "!");
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.message = resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "!";
    }

    public enum ErrorType {
        CREATED, FOUND, UPDATED, DELETED
    }
}
