package com.nhnacademy.twojoping.service;

import com.nhnacademy.twojoping.exception.MemberNotFoundException;
import com.nhnacademy.twojoping.model.Admin;
import com.nhnacademy.twojoping.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin getAdmin(String id) {
        return adminRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
    }
}
