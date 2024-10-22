package com.nhnacademy.twojoping.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String id) {
        super("Member with id " + id + " not found");
    }
}
