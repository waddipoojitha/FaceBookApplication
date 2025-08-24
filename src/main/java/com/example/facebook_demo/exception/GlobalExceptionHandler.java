package com.example.facebook_demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", error);
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return new ResponseEntity<>(response, status);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String,Object>> handleInvalidCredentials(InvalidCredentialsException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED,"INVALID_CREDENTIALS",ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, "ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidToken(InvalidTokenException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN", ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<Map<String, Object>> handleUnauthorizedAction(UnauthorizedActionException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "UNAUTHORIZED_ACTION", ex.getMessage());
    }

    @ExceptionHandler(MediaUploadException.class)
    public ResponseEntity<Map<String, Object>> handleMediaUpload(MediaUploadException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "MEDIA_UPLOAD_ERROR", ex.getMessage());
    }

    @ExceptionHandler(FriendRequestException.class)
    public ResponseEntity<Map<String, Object>> handleFriendRequestException(FriendRequestException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "FRIEND_REQUEST_ERROR", ex.getMessage());
    }
}