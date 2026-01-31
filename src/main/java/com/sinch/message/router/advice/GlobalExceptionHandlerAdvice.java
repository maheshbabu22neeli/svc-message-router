package com.sinch.message.router.advice;

import com.sinch.message.router.exceptions.ResourceNotFoundException;
import com.sinch.message.router.exceptions.ValidationException;
import com.sinch.message.router.models.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

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

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(
            ValidationException validationException) {

        log.error("ResourceNotFoundException raised", validationException);

        ErrorMessageResponse errorMessageResponse = new ErrorMessageResponse(
                HttpStatus.BAD_REQUEST.value(),
                validationException.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessageResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .toList();

        return ResponseEntity.badRequest()
                .body(new ErrorMessageResponse(400, errors.toString()));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleNoResourceFoundException(
            NoResourceFoundException ex) {

        return ResponseEntity.badRequest()
                .body(new ErrorMessageResponse(400, ex.getMessage()));
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
