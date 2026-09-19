package com.app.expenseautomator.config;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.app.expenseautomator.exceptions.UserAlreadyExistsException;
import com.app.expenseautomator.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ExceptionResponseConfig extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ProblemDetail handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return getProblemTemplate(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex) {
        return getProblemTemplate(HttpStatus.NOT_FOUND, ex);
    }
    
    @ExceptionHandler(NullPointerException.class)
    public ProblemDetail handleUserCreationException() {
        return getProblemTemplate(HttpStatus.INTERNAL_SERVER_ERROR , new NullPointerException("Something went wrong."));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request
    ) {

        Map<String, Map<String, String>> wrMap = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        wrMap.put("invalid", errors);
        return new ResponseEntity<>(wrMap, HttpStatus.BAD_REQUEST);
    }

    public ProblemDetail getProblemTemplate(HttpStatusCode status, Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("message", ex.getMessage());
        properties.put("timestamp", Instant.now());
        problemDetail.setProperties(properties);

        return problemDetail;
    }
}
