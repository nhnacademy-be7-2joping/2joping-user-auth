package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.LoginReqDto;
import com.nhnacademy.twojoping.dto.LoginResDto;
import com.nhnacademy.twojoping.model.Member;
import com.nhnacademy.twojoping.security.MemberUserDetails;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import com.nhnacademy.twojoping.service.MemberUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final MemberUserDetailService memberUserDetailService;
}
