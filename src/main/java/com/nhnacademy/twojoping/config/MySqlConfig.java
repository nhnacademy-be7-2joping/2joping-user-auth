package com.nhnacademy.twojoping.config;

import com.nhnacademy.twojoping.dto.response.MysqlKeyResponseDto;
import com.nhnacademy.twojoping.service.KeyManagerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class MySqlConfig {
    private final KeyManagerService keyManagerService;

    @Bean
    public DataSource dataSource() {
        MysqlKeyResponseDto keyResponseDto = keyManagerService.getDbConnectionInfo();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(keyResponseDto.url());
        dataSource.setUsername(keyResponseDto.username());
        dataSource.setPassword(keyResponseDto.password());

        // DBCP2 커넥션 풀 설정
        dataSource.setInitialSize(5);
        dataSource.setMaxTotal(20);
        dataSource.setMaxIdle(10);
        dataSource.setMinIdle(5);
        dataSource.setMaxWait(Duration.ofSeconds(10)); // 10 초

        return dataSource;
    }
}

