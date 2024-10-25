package com.nhnacademy.twojoping.common.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsWithId extends UserDetails {
    Long getId();
}
