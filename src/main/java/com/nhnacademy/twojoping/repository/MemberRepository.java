package com.nhnacademy.twojoping.repository;

import com.nhnacademy.twojoping.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByCustomerId(Long customerId);
    Optional<Member> findByLoginId(String id);
}
