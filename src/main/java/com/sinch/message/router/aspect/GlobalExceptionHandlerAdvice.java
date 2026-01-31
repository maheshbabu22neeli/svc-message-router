package com.sinch.message.router.aspect;

import com.sinch.message.router.exceptions.ResourceNotFoundException;
import com.sinch.message.router.models.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandlerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNotFoundException(
            ResourceNotFoundException resourceNotFoundException) {

        log.error("ResourceNotFoundException raised", resourceNotFoundException);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.NOT_FOUND.value(),
                resourceNotFoundException.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessageResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessageResponse> handleAnyException(
            Exception exception) {

        log.error("Exception raised", exception);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Something went wrong, please try again later"
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessageResponse);
    }

}
