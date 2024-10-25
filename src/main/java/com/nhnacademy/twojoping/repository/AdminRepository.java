package com.nhnacademy.twojoping.repository;

import com.nhnacademy.twojoping.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findById(String id);
}
