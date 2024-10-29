package com.nhnacademy.twojoping.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class AccessTokenResponseDto {

    @JsonProperty("accessToken")
    private String accessToken;

    public AccessTokenResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
