package com.nhnacademy.twojoping.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;


public record AccessTokenResponseDto(
        @JsonProperty("accessToken")
        String accessToken
) {

}
