package com.nhnacademy.twojoping.security;

import com.nhnacademy.twojoping.common.security.UserDetailsWithId;
import com.nhnacademy.twojoping.model.Admin;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;


public class AdminUserDetails implements UserDetailsWithId {
    private final Collection<SimpleGrantedAuthority> authorities;
    @Getter
    private final Admin admin;

    public AdminUserDetails(Admin admin) {
        this.admin = admin;
        authorities = new ArrayList<SimpleGrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return admin.getPassword();
    }

    @Override
    public String getUsername() {
        return admin.getId();
    }

    @Override
    public Long getId() {
        return admin.getAdminId();
    }

    @Override
    public String getNickName() {
        return "admin";
    }
}
