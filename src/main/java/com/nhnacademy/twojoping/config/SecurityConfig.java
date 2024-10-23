package com.nhnacademy.twojoping.config;

import com.nhnacademy.twojoping.filter.JsonLoginRequestFilter;
import com.nhnacademy.twojoping.handler.MemberLoginFailureHandler;
import com.nhnacademy.twojoping.handler.MemberLoginSuccessHandler;
import com.nhnacademy.twojoping.security.provider.MemberAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final MemberLoginFailureHandler memberLoginFailureHandler;
    private final MemberLoginSuccessHandler memberLoginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager manager) throws Exception {
        http.authorizeHttpRequests(
                request -> {
                    request.requestMatchers("/login/**", "/login", "/", "/error/**").permitAll()
                            .anyRequest().authenticated();
                }
        );

        JsonLoginRequestFilter jsonLoginRequestFilter = new JsonLoginRequestFilter();
        jsonLoginRequestFilter.setAuthenticationManager(manager);
        jsonLoginRequestFilter.setAuthenticationSuccessHandler(memberLoginSuccessHandler);
        jsonLoginRequestFilter.setAuthenticationFailureHandler(memberLoginFailureHandler);
        http.addFilterBefore(jsonLoginRequestFilter, UsernamePasswordAuthenticationFilter.class);

        // CSRF 비활성화
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(memberAuthenticationProvider(passwordEncoder))
                .build();
    }

    @Bean
    public MemberAuthenticationProvider memberAuthenticationProvider(PasswordEncoder passwordEncoder) {
        return new MemberAuthenticationProvider(userDetailsService, passwordEncoder);
    }
}
