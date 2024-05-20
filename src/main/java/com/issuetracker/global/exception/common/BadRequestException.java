package com.issuetracker.global.exception.common;

import com.issuetracker.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends GlobalException {
    public BadRequestException() {
        super(HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String errorMessage) {
        super(HttpStatus.BAD_REQUEST, errorMessage);
    }
}
