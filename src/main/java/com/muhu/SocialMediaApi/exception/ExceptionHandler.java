package com.muhu.SocialMediaApi.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDate;
import java.util.Map;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(DuplicateException.class)
    public ResponseEntity<?> duplicateException(DuplicateException exception, HttpServletRequest request){
        Map<String,String> errors =Map.of(
                "timestamp", LocalDate.now().toString(),
                "status",HttpStatus.CONFLICT.toString(),
                "massage",exception.getMessage(),
                "path",request.getServletPath()
        );
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errors);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException exception , HttpServletRequest request){
        Map<String,String> errors =Map.of(
                "timestamp", LocalDate.now().toString(),
                "status",HttpStatus.NOT_FOUND.toString(),
                "massage",exception.getMessage(),
                "path",request.getServletPath()
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errors);
    }
}
