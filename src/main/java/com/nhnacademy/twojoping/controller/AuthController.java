//package com.nhnacademy.twojoping.controller;
//
//import com.nhnacademy.twojoping.dto.LoginRequest;
//import com.nhnacademy.twojoping.security.provider.JwtTokenProvider;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//public class AuthController {
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenProvider jwtTokenProvider;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
//        try {
//            String username = loginRequest.getUsername();
//            String password = loginRequest.getPassword();
//
//            Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            List<String> roles = authentication.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.toList());
//
//
//            String token = jwtTokenProvider.generateToken(username, roles);
//            return ResponseEntity.ok(token);
//        } catch (BadCredentialsException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login failed"); // 401 반환
//        }
//    }
//
//    @GetMapping("/logout")
//    public String logout() {
//        return "logout";
//    }
//}
