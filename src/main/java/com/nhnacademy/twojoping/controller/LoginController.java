package com.nhnacademy.twojoping.controller;

import com.nhnacademy.twojoping.dto.LoginReqDto;
import com.nhnacademy.twojoping.model.Member;
import com.nhnacademy.twojoping.security.MemberUserDetails;
import com.nhnacademy.twojoping.service.MemberUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final MemberUserDetailService memberUserDetailService;

    @PostMapping("/login")
    public Member doLogin(@RequestBody LoginReqDto loginReqDto) {
        MemberUserDetails details = memberUserDetailService.loadUserByUsername(loginReqDto.id());

        return details.getMember();
    }
}
