package ru.otus.hw14.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "demoDbDataSource")
    @ConfigurationProperties("spring.datasource.demo-db")
    public DataSource demoDbDataSource(@Value("${spring.datasource.demo-db.url}") String url,
                                       @Value("${spring.datasource.demo-db.username}") String username,
                                       @Value("${spring.datasource.demo-db.password}") String password) {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .build();
    }
}
