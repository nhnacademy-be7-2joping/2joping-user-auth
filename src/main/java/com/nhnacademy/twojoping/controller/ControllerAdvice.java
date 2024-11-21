package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.response.ErrorDto;
import com.nhnacademy.twojoping.exception.InvalidRefreshToken;
import com.nhnacademy.twojoping.exception.MemberNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({MemberNotFoundException.class})
    public ResponseEntity<ErrorDto> notFound(MemberNotFoundException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.NOT_FOUND.value(), "Not Found", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorDto> badCredentials(BadCredentialsException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

    @ExceptionHandler({InvalidRefreshToken.class})
    public ResponseEntity<ErrorDto> invalidRefreshToken(InvalidRefreshToken e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.FORBIDDEN.value(), "Invalid refresh Token", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDto);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<ErrorDto> expiredJwtException(ExpiredJwtException e) {
        ErrorDto errorDto = new ErrorDto(HttpStatus.UNAUTHORIZED.value(), "TOKEN_EXPIRED", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDto);
    }

}
