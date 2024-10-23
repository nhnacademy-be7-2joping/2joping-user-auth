package com.nhnacademy.twojoping.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginReqDto(@NotBlank String id, @NotBlank String password) {
}
