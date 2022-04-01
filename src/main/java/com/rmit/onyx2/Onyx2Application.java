package com.rmit.onyx2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title="OnyxAPI", version = "1.0", description = "This is a backend for Onyx Task Management System"))
public class Onyx2Application implements CommandLineRunner {
    static final String url = "123";
    public static void main(String[] args) {
        SpringApplication.run(Onyx2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {}
}
