package com.nhnacademy.twojoping.exception;

import org.springframework.security.core.AuthenticationException;

public class AccountDormantException extends AuthenticationException {
    public AccountDormantException(String msg) {
        super(msg);
    }
}