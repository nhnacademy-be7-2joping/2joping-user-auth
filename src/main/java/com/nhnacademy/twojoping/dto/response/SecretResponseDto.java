package com.nhnacademy.twojoping.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class SecretResponseDto {
    private Header header;
    private Body body;

    @Getter
    @NoArgsConstructor
    public static class Header {
        private int resultCode;
        private String resultMessage;
        private boolean isSuccessful;
    }

    @Getter
    @NoArgsConstructor
    public static class Body {
        private String secret;
    }
}
