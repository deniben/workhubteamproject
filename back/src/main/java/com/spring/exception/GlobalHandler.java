package com.spring.exception;

import java.util.Date;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(GlobalHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        LOGGER.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), ex.getLocalizedMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity noSuchElementException(NoSuchElementException ex, WebRequest request) {
		LOGGER.info(ex.getMessage());
		ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), ex.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}


    @ExceptionHandler(ResourceForbiddenException.class)
    public ResponseEntity forbiddenException(ResourceForbiddenException ex, WebRequest request) {
        LOGGER.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), ex.getLocalizedMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);

    }

    @ExceptionHandler(OtherException.class)
    public ResponseEntity forbiddenException(OtherException ex, WebRequest request) {
        LOGGER.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(new Date(), ex.getMessage(), ex.getLocalizedMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ErrorResponse> restExceptionHandler(RestException restException) {
        LOGGER.debug(restException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(new Date(), restException.getMessage(), restException.getLocalizedMessage());
        return ResponseEntity.status(restException.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity validationHandler(ValidationException validationException) {
        LOGGER.debug(validationException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(new Date(), validationException.getValidation(), validationException.getLocalizedMessage());
        return ResponseEntity.status(validationException.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> securityException(SecurityException securityException) {
        LOGGER.debug("Security exception : " + securityException.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(new Date(), securityException.getMessage(), securityException.getDescription());
        return ResponseEntity.status(securityException.getStatus()).body(errorResponse);
    }
}
