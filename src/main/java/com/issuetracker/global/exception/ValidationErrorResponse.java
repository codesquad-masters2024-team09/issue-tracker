package com.issuetracker.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ValidationErrorResponse {

    private final Integer errorCode;
    private final String errorMessage;
    List<ValidationErrorDetails> errors;
}
