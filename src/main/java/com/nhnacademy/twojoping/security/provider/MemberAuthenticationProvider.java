package com.nhnacademy.twojoping.security.provider;

import com.nhnacademy.twojoping.exception.AccountDormantException;
import com.nhnacademy.twojoping.exception.MemberNotFoundException;
import com.nhnacademy.twojoping.security.MemberUserDetails;
import com.nhnacademy.twojoping.service.AdminUserDetailService;
import com.nhnacademy.twojoping.service.MemberUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberAuthenticationProvider implements AuthenticationProvider {
    private final MemberUserDetailService memberUserDetailService;
    private final AdminUserDetailService adminUserDetailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();
        UserDetails userDetails;

        try {
            // 사용자 정보
            userDetails = memberUserDetailService.loadUserByUsername(id);
            // 휴면 계정 처리

            if (!userDetails.isAccountNonLocked()) {
                throw new AccountDormantException("계정이 휴면 상태입니다. 휴면 해제를 진행해주세요.");
            }

            // 비밀번호 비교
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                // 인증 성공
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            }
        } catch (MemberNotFoundException e) {
            // 회원 계정에 일치 계정 없을 시 관리자 조회
            userDetails = adminUserDetailService.loadUserByUsername(id);
            if (passwordEncoder.matches(password, userDetails.getPassword())) {
                return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
            }
        }

        throw new BadCredentialsException("Invalid password");
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
