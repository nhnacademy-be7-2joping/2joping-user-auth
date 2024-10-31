package com.nhnacademy.twojoping.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(@NotBlank String id, @NotBlank String password) {
}
