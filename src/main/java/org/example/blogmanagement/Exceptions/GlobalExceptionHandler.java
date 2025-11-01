package org.example.blogmanagement.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value={ResourceAlreadyExists.class})
    public ResponseEntity<String> handleResourceAlreadyExistsException (ResourceAlreadyExists resourceAlreadyExists){
        return new ResponseEntity<>(resourceAlreadyExists.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value={ResourceNotFound.class})
    public ResponseEntity<String> handleResourceNotFoundException (ResourceNotFound resourceNotFound){
        return new ResponseEntity<>(resourceNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value={MethodArgumentNotValidException.class})
    public Map<String,String> handleValidationExceptions (MethodArgumentNotValidException ex){

        Map<String,String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                (error) -> {
                    String fieldName = error.getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName,errorMessage);
                }
        );

        return errors;
    }



}

