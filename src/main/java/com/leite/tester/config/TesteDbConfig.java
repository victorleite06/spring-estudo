package com.leite.tester.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.intercept.RunAsManager;
import org.springframework.security.access.intercept.RunAsManagerImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import javax.sql.DataSource;

@Configuration
@EnableMethodSecurity(securedEnabled = false)
public class TesteDbConfig {
    @Bean
    public DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.hibernate.dialect.SQLServerDialect");
        dataSourceBuilder.url("jdbc:sqlserver://localhost:1433;databaseName=TesteDb;trustServerCertificate=true;encrypt=false");
        dataSourceBuilder.username("api");
        dataSourceBuilder.password("123");
        return dataSourceBuilder.build();
    }
}