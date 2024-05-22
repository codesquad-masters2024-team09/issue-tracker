package com.issuetracker.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static com.issuetracker.global.exception.ExceptionType.METHOD_ARGUMENT_NOT_VALID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(GlobalException e){
        log.error(getExceptionStackTrace(e));

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponse(e.getCode(), e.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status, @NonNull WebRequest request) {
        ExceptionType methodArgumentNotValid = METHOD_ARGUMENT_NOT_VALID;
        log.error(getExceptionStackTrace(ex));

        List<ValidationErrorDetails> validationErrorDetails = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationErrorDetails(
                        fieldError.getField(),
                        fieldError.getCode(),
                        fieldError.getDefaultMessage()
                )).toList();

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ValidationErrorResponse(
                        methodArgumentNotValid.getCode(),
                        methodArgumentNotValid.getMessage(),
                        validationErrorDetails
                ));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            @NonNull Exception ex, @Nullable Object body, @NonNull HttpHeaders headers, @NonNull HttpStatusCode statusCode, @NonNull WebRequest request) {
        log.error("[Spring MVC Exception] {}", getExceptionStackTrace(ex));

        ExceptionType exceptionType = ExceptionType.of(ex.getClass()).orElse(ExceptionType.UNHANDLED);
        return ResponseEntity
                .status(statusCode)
                .body(new ErrorResponse(exceptionType));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("[UnHandled Exception] {}", getExceptionStackTrace(ex));

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        ExceptionType.UNHANDLED.getCode(),
                        ExceptionType.UNHANDLED.getMessage() + " " + ex.getMessage()
                ));
    }

    private String getExceptionStackTrace(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return String.valueOf(stringWriter);
    }
}
