package com.issuetracker.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationErrorDetails {

    private final String field;
    private final String checkedBy;
    private final String errorMessage;
}
