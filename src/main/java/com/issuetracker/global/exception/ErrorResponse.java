package com.issuetracker.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final Integer errorCode;
    private final String errorMessage;

    public ErrorResponse(GlobalException e) {
        this.errorCode = e.getCode();
        this.errorMessage = e.getMessage();
    }

    public ErrorResponse(ExceptionType exceptionType) {
        this.errorCode = exceptionType.getCode();
        this.errorMessage = exceptionType.getMessage();
    }
}
