package com.nhnacademy.twojoping.config;

import com.nhnacademy.twojoping.dto.response.MysqlKeyResponseDto;
import com.nhnacademy.twojoping.service.KeyManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MySqlConfig {
    private final KeyManagerService keyManagerService;

    @Bean
    public DataSource dataSource() {
        MysqlKeyResponseDto keyResponseDto = keyManagerService.getDbConnectionInfo();
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(keyResponseDto.url());
        dataSource.setUsername(keyResponseDto.username());
        dataSource.setPassword(keyResponseDto.password());
        return dataSource;
    }
}

