package com.example.githubrepolistingapi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {

        ExceptionResponse error = new ExceptionResponse();
        error.setStatus(ex.getStatusCode().value());
        error.setMessage("Client must accept application/json");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(error, responseHeaders, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionResponse> handleHttpClientError(HttpClientErrorException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse();
        errorResponse.setStatus(ex.getStatusCode().value());

        if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            errorResponse.setMessage("User with given username not found");
        } else {
            errorResponse.setStatus(ex.getStatusCode().value());
            errorResponse.setMessage(ex.getMessage());
        }

        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }
}
