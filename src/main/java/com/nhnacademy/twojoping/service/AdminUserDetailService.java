package com.nhnacademy.twojoping.service;

import com.nhnacademy.twojoping.common.security.AbstractUserDetailService;
import com.nhnacademy.twojoping.model.Admin;
import com.nhnacademy.twojoping.security.AdminUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminUserDetailService extends AbstractUserDetailService<Admin> {
    private final AdminService adminService;

    @Override
    protected Admin findUser(String username) {
        return adminService.getAdmin(username);
    }

    @Override
    protected UserDetails createUserDetails(Admin user) {
        return new AdminUserDetails(user);
    }
}
