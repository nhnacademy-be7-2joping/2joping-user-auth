package com.nhnacademy.twojoping.common.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public abstract class AbstractUserDetailService<T> implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        T user = findUser(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return createUserDetails(user);
    }

    protected abstract T findUser(String username);

    protected abstract UserDetails createUserDetails(T user);
}
