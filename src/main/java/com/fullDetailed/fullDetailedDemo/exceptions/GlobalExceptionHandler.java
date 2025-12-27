package com.fullDetailed.fullDetailedDemo.exceptions;

import com.fullDetailed.fullDetailedDemo.exceptions.pojo.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFound(NotFoundException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequest(BadRequestException ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneral(Exception ex, HttpServletRequest request) {

        ErrorResponse error = new ErrorResponse(
                "Something went wrong!",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<?>handleAlreadyExists(AlreadyExistsException ex,HttpServletRequest request){
        ErrorResponse error=new ErrorResponse(
                ex.getMessage(),
                HttpStatus.CONFLICT.value(),
                request.getRequestURI(),
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
