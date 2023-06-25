package com.login.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClientErrorException(HttpClientErrorException ex, WebRequest request) {
        HttpStatus status = (HttpStatus) ex.getStatusCode();
        String errorBody = ex.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(status, errorBody);
        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), status, request);
    }

    @Getter
    @AllArgsConstructor
    private static class ErrorResponse {
        private HttpStatus status;
        private String message;
    }
}