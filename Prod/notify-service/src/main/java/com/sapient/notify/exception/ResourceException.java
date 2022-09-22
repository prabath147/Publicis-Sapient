package com.sapient.notify.exception;

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
public class ResourceException extends RuntimeException{
    private final String resourceName;
    private final String fieldName;
    private final transient Object fieldValue;

    public ResourceException(String resourceName, String fieldName, Object fieldValue, ErrorType errorType, Exception e) {
        super(resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "!");
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        log.info(e.toString());
    }

    public ResourceException(String resourceName, String fieldName, Object fieldValue, ErrorType errorType) {
        super(resourceName + " with " + fieldName + " = " + fieldValue.toString() + " not " + errorType + "!");
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public enum ErrorType {
        CREATED,
        FOUND,
        UPDATED,
        DELETED
    }
}
