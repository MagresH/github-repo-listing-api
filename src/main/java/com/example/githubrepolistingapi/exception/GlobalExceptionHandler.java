package com.example.githubrepolistingapi.exception;

import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ExceptionResponse> createResponseEntity(int status, String message) {
        ExceptionResponse errorResponse = new ExceptionResponse();
        errorResponse.setStatus(status);
        errorResponse.setMessage(message);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(errorResponse, responseHeaders, HttpStatusCode.valueOf(status));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ExceptionResponse> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        return createResponseEntity(ex.getStatusCode().value(), "Client must accept application/json");
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ExceptionResponse> handleHttpClientError(HttpClientErrorException ex) {
        String message = ex.getStatusCode() == HttpStatus.NOT_FOUND ? "User with given username not found" : ex.getMessage();

        return createResponseEntity(ex.getStatusCode().value(), message);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ExceptionResponse> handleForbidden(HttpClientErrorException.Forbidden ex){
        if (ex.getMessage().contains("API rate limit exceeded")) {
            return createResponseEntity(ex.getStatusCode().value(), "Github API free rate limit exceeded. Please try again later.");
        }
        return createResponseEntity(ex.getStatusCode().value(), ex.getMessage());
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ExceptionResponse> handleException(RestClientResponseException ex) {
        return createResponseEntity(ex.getStatusCode().value(), ex.getMessage());
    }


}
