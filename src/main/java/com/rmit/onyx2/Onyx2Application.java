package com.rmit.onyx2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Onyx2Application implements CommandLineRunner {
    static final String url = "123";
    public static void main(String[] args) {
        SpringApplication.run(Onyx2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {}
}
