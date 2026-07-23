package me.elpomoika.UserService.exception;

import me.elpomoika.UserService.dto.ApiError;
import me.elpomoika.UserService.exception.file.FileProcessingException;
import me.elpomoika.UserService.exception.file.FileTooLargeException;
import me.elpomoika.UserService.exception.file.InvalidFileTypeException;
import me.elpomoika.UserService.exception.user.UserExistsException;
import me.elpomoika.UserService.exception.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileTooLargeException.class)
    public ResponseEntity<ApiError> handleFileTooLarge(FileTooLargeException e) {
        return ResponseEntity
                .status(HttpStatus.CONTENT_TOO_LARGE)
                .body(new ApiError("CONTENT_TOO_LARGE", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFound(UserNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ApiError("NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ApiError> handleInvalidFileType(InvalidFileTypeException e) {
        return ResponseEntity
                .badRequest()
                .body(new ApiError("INVALID_FILE_TYPE", e.getMessage()));
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<ApiError> handleFileProcessing(FileProcessingException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError("FILE_PROCESSING_ERROR", ex.getMessage()));
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ApiError> handleUserExists(UserExistsException ex) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ApiError("USER_EXISTS", ex.getMessage()));
    }
}
