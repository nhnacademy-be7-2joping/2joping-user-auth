package com.nhnacademy.twojoping.security;

import com.nhnacademy.twojoping.model.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class MemberUserDetails implements UserDetails {
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
        return member.getId();
    }

}
