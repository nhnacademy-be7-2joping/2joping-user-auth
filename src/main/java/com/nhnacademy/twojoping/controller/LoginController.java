package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.LoginNonMemberReqDto;
import com.nhnacademy.twojoping.dto.LoginResDto;
import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
import com.nhnacademy.twojoping.service.MemberUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final MemberUserDetailService memberUserDetailService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login/non-member")
    public ResponseEntity<LoginResDto> doLoginNonMember(@RequestBody LoginNonMemberReqDto nonMemberReqDto) {
        return ResponseEntity.ok().build();
    }
}
