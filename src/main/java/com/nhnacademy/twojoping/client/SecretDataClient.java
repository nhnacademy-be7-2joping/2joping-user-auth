package com.nhnacademy.twojoping.client;

import com.nhnacademy.twojoping.dto.response.SecretResponseDto;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "secretClient", url = "${keymanager.url}")
public interface SecretDataClient {
    @GetMapping("/appkey/{appKey}/secrets/{keyId}")
    SecretResponseDto getSecret(@PathVariable("appKey") String appkey, @PathVariable("keyId") String keyId,
                                @RequestHeader("X-TC-AUTHENTICATION-ID") String authenticationId,
                                @RequestHeader("X-TC-AUTHENTICATION-SECRET") String authenticationSecret);
}
