package com.nhnacademy.twojoping.service;

import com.nhnacademy.twojoping.common.security.AbstractUserDetailService;
import com.nhnacademy.twojoping.model.Member;
import com.nhnacademy.twojoping.security.MemberUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberUserDetailService extends AbstractUserDetailService<Member> {
    private final MemberService memberService;

    @Override
    protected Member findUser(String username) {
        return memberService.getMember(username);
    }

    @Override
    protected UserDetails createUserDetails(Member user) {
        return new MemberUserDetails(user);
    }
}
