package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.response.ErrorDto;
import com.nhnacademy.twojoping.exception.InvalidRefreshToken;
import com.nhnacademy.twojoping.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({MemberNotFoundException.class})
    public ResponseEntity<ErrorDto> notFound(MemberNotFoundException e) {
        ErrorDto errorDto = new ErrorDto(404, "Not Found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorDto> badCredentials(BadCredentialsException e) {
        ErrorDto errorDto = new ErrorDto(401, "Unauthorized", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler({InvalidRefreshToken.class})
    public ResponseEntity<ErrorDto> invalidRefreshToken(InvalidRefreshToken e) {
        ErrorDto errorDto = new ErrorDto(403, "Invalid Refresh Token", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }
}
