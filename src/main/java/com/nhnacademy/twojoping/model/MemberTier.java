package com.nhnacademy.twojoping.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberTier {
    @Size(max = 20)
    @NotNull
    @Column(unique = true)
    String name;

    @NotNull
    Boolean status;

    @NotNull
    Byte accRate;

    @NotNull
    int promotion;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_tier_id")
    private Long id;
}
