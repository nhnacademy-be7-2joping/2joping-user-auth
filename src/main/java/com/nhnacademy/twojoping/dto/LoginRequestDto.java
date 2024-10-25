package com.nhnacademy.twojoping.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(@NotBlank String id, @NotBlank String password) {
}
