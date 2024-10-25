package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final JwtTokenProvider jwtTokenProvider;
}
