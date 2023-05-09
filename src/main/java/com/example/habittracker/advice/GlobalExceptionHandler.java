package com.example.habittracker.advice;


import com.example.habittracker.exceptions.EmailAlreadyExistsException;
import com.example.habittracker.exceptions.NotFoundException;
import com.example.habittracker.exceptions.ResetPasswordCodeExpirationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiError err = new ApiError(
                status.value(),
                LocalDateTime.now(),
                "No Handler Found");
        return new ResponseEntity<Object>(err, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<String>();
        details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getObjectName()+ " : " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ApiError err = new ApiError(
                status.value(),
                LocalDateTime.now(),
                "Validation Errors",
                details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      WebRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                        ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(
            Exception ex,
            WebRequest request) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "Constraint Violation" ,
                details);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(
            NotFoundException ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                "Resource Not Found",
                details);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(value = EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailExistsException(EmailAlreadyExistsException ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                "Email is already taken",
                details);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeException(MaxUploadSizeExceededException ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                "File too large!",
                details);

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(err);
    }

    @ExceptionHandler(value = ResetPasswordCodeExpirationException.class)
    public ResponseEntity<Object> handleResetPasswordCodeExpirationException(ResetPasswordCodeExpirationException ex) {

        List<String> details = new ArrayList<String>();
        details.add(ex.getMessage());

        ApiError err = new ApiError(
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now(),
                "Reset password code expired!",
                details);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }

}
