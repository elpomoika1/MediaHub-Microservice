package me.elpomoika.AuthenticationService.presentation.advice;

import me.elpomoika.AuthenticationService.application.dto.ApiError;
import me.elpomoika.AuthenticationService.domain.exception.UserExistsException;
import me.elpomoika.AuthenticationService.domain.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError("NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ApiError> handleUserExists(UserExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiError("USER_EXISTS", ex.getMessage()));
    }
}
