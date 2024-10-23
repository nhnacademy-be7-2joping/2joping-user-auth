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
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<LoginResDto> doLogin(@RequestBody LoginReqDto loginReqDto) {
        //사용자 정보 load
        MemberUserDetails details = memberUserDetailService.loadUserByUsername(loginReqDto.id());

        // JWT 토큰 생성
        String token = jwtTokenProvider.generateToken(loginReqDto.id(), details.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        //토큰 담기
        LoginResDto loginResDto = new LoginResDto(details.getMember(), token);

        return ResponseEntity.ok(loginResDto);
    }
}
