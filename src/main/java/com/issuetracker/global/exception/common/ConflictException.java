package com.issuetracker.global.exception.common;

import com.issuetracker.global.exception.GlobalException;
import org.springframework.http.HttpStatus;

public class ConflictException extends GlobalException {

    public ConflictException() {
        super(HttpStatus.CONFLICT);
    }

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
