package com.nhnacademy.twojoping.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Customer {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_status_id", nullable = false)
    private MemberStatus memberStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_tier_id", nullable = false)
    private MemberTier memberTier;

    @Column(name = "nickname", length = 20, nullable = false)
    private String nickname;

    @Column(name = "login_id", length = 20, nullable = false, unique = true)
    private String id;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @Column(name = "gender")
    private Boolean gender;

    @Column(name = "birthday")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "join_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime joinDate;

    @Column(name = "recent_login_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime recentLoginDate;

    @Column(name = "is_payco_login", nullable = false)
    private Boolean isPaycoLogin;

    @Column(name = "point", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int point;

    @Column(name = "acc_purchase", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int accPurchase;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    Customer customer;
}
