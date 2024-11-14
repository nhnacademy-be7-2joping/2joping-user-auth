package com.nhnacademy.twojoping.repository;

import com.nhnacademy.twojoping.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByCustomerId(Long customerId);
    Optional<Member> findByLoginId(String loginId);

    @Query("SELECT c.customerId FROM Customer c WHERE c.member.loginId = :loginId")
    long findCustomerIdByLoginId(@Param("loginId")String loginId);
}
