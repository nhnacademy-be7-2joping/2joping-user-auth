package com.nhnacademy.twojoping.exception;

public class InvalidRefreshToken extends RuntimeException {
    public InvalidRefreshToken() {
        super("Invalid refresh token");
    }
}
