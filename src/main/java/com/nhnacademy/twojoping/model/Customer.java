package com.nhnacademy.twojoping.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Long id;

    @NotNull
    @Size(max = 20)
    private String name;

    @NotNull
    @Column(unique = true, nullable = false, length = 20)
    private String phone;

    @NotNull
    @Email
    @Column(unique = true, nullable = false, length = 50)
    private String email;
}
