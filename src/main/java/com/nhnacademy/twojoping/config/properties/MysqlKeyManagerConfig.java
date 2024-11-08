package com.nhnacademy.twojoping.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mysql-info")
@Getter
@Setter
public class MysqlKeyManagerConfig {
    private String url;
    private String username;
    private String password;
}
