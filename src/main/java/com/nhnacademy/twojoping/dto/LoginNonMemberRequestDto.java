package com.nhnacademy.twojoping.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginNonMemberRequestDto(@NotBlank String name, @NotBlank String password, @NotBlank String phone,
                                       @NotBlank String email) {
}
