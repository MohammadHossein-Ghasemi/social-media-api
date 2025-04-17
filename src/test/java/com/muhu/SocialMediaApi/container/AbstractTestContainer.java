package com.muhu.SocialMediaApi.container;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractTestContainer {

    @Container
    protected static final PostgreSQLContainer<?> psqlContainer=
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("muhu-repo-unit-test")
                    .withUsername("muhu")
                    .withPassword("password");

    @BeforeAll
    static void beforeAll(){
        Flyway flyway = Flyway.configure().dataSource(
                psqlContainer.getJdbcUrl(),
                psqlContainer.getUsername(),
                psqlContainer.getPassword()
        ).load();
        flyway.migrate();
    }

    @DynamicPropertySource
    private static void registerDataSourceProperties(DynamicPropertyRegistry propertyRegistry){
        propertyRegistry.add("spring.datasource.url",psqlContainer::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username",psqlContainer::getUsername);
        propertyRegistry.add("spring.datasource.password",psqlContainer::getPassword);
    }
}
