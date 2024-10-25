package com.nhnacademy.twojoping.dto;

import com.nhnacademy.twojoping.model.Member;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;


public record LoginResDto (@NotBlank Member member, @NotBlank String token) {}

