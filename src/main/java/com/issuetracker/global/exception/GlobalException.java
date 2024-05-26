package com.issuetracker.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
    private final Throwable cause;

    public GlobalException(HttpStatus httpStatus) {
        ExceptionType exceptionType = getExceptionType();
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
        this.cause = null;
    }

    public GlobalException(HttpStatus httpStatus, String message) {
        ExceptionType exceptionType = getExceptionType();
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = message;
        this.cause = null;
    }

    public GlobalException(HttpStatus httpStatus, Throwable cause) {
        ExceptionType exceptionType = getExceptionType();
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = exceptionType.getMessage();
        this.cause = cause;
    }

    public GlobalException(HttpStatus httpStatus, String message, Throwable cause) {
        ExceptionType exceptionType = getExceptionType();
        this.httpStatus = httpStatus;
        this.code = exceptionType.getCode();
        this.message = message;
        this.cause = cause;
    }

    private ExceptionType getExceptionType() {
        return ExceptionType.of(this.getClass()).orElse(ExceptionType.UNHANDLED);
    }

}
