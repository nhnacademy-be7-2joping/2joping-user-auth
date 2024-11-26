package com.nhnacademy.twojoping.service;

import com.nhnacademy.twojoping.exception.MemberNotFoundException;
import com.nhnacademy.twojoping.model.Admin;
import com.nhnacademy.twojoping.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin getAdmin(String id) {
        log.info("for Test");
        return adminRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }
}
