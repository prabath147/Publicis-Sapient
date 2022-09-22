package com.pharmacy.service.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@Getter
@Setter
@ToString
@Slf4j
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ExcelException extends IOException {
    private final String errorMessage;

    public ExcelException(String errorMessage, Exception e) {
        super(errorMessage);
        this.errorMessage = errorMessage;
        log.info(e.toString());
    }

    public ExcelException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }
}
