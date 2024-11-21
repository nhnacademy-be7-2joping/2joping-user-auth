package com.nhnacademy.twojoping.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorDto(int status, String errorCode, String message) {
}
