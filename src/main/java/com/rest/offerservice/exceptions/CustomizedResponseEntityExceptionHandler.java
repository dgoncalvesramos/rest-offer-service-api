package com.rest.offerservice.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(OfferNotFoundException.class)
    public final ResponseEntity<ErrorDetails> handleOfferNotFoundException(OfferNotFoundException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(Instant.now(), ex.toString(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OfferBadFormatException.class)
    public final ResponseEntity<ErrorDetails> handleOfferBadFormartException(OfferBadFormatException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(Instant.now(), ex.toString(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OfferPatchException.class)
    public final ResponseEntity<ErrorDetails> handleOfferPatchException(OfferPatchException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(Instant.now(), ex.toString(),
                request.getDescription(false));

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(Instant.now(), ex.toString(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
