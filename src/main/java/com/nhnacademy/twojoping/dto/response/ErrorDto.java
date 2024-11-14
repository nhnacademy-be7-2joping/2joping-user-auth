package com.nhnacademy.twojoping.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorDto(HttpStatus status, String errorCode, String errorMessage) {
}
