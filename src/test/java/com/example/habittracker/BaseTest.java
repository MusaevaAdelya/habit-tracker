package com.example.habittracker;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class BaseTest {

     static PostgreSQLContainer postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer("postgres:latest")
            .withDatabaseName("habit-tracker")
            .withUsername("testuser")
            .withPassword("pass")
            .withReuse(true);

    static {
        postgreSQLContainer.start();
    }
}