package com.nhnacademy.twojoping.service;

import com.nhnacademy.twojoping.exception.MemberNotFoundException;
import com.nhnacademy.twojoping.model.Member;
import com.nhnacademy.twojoping.security.MemberUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberUserDetailService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final MemberService memberService;

    @Override
    public MemberUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Member member = memberService.getMember(username);
            if (member == null) {
                throw new MemberNotFoundException(username);
            }

            return new MemberUserDetails(member);
        } catch (MemberNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
