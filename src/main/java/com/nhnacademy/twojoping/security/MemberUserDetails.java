package com.nhnacademy.twojoping.security;

import com.nhnacademy.twojoping.common.security.UserDetailsWithId;
import com.nhnacademy.twojoping.model.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

public class MemberUserDetails implements UserDetailsWithId {
    @Getter
    private Member member;
    private final Collection<SimpleGrantedAuthority> authorities;

    public MemberUserDetails(Member member) {
        this.member = member;
        this.authorities = new ArrayList<SimpleGrantedAuthority>();

        authorities.add(new SimpleGrantedAuthority("ROLE_MEMBER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public Long getId() {
        return member.getCustomerId();
    }

    @Override
    public boolean isAccountNonLocked() {

        return member.getMemberStatus().getId() != 2;
    }

    @Override
    public boolean isEnabled() {
        // 가입(1) 상태만 활성화
        return member.getMemberStatus().getId() != 1;
    }
    @Override
    public String getNickName() { return member.getNickname();
    }
}
