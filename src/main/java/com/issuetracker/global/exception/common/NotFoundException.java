package com.issuetracker.global.exception.common;

import com.issuetracker.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends GlobalException {

    public NotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
