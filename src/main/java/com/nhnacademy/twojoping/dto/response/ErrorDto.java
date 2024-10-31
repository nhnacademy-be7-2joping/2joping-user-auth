package com.nhnacademy.twojoping.dto.response;

public record ErrorDto(int status, String errorCode, String message) {
}
