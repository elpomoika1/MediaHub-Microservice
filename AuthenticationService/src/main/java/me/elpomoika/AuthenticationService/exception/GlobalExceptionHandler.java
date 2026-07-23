package me.elpomoika.AuthenticationService.exception;

import me.elpomoika.AuthenticationService.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
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
