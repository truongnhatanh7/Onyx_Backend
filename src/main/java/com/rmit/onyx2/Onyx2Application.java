package com.rmit.onyx2;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title="OnyxAPI", version = "1.0", description = "This is a backend for Onyx Task Management System"))
public class Onyx2Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Onyx2Application.class, args);
    }

//    @Autowired
//    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
//        User nanh = new User();
//        nanh.setName("Truong Nhat Anh");
//        nanh.setUsername("truongnhatanhh");
//        nanh.setPassword("123456");
//
//        Workspace sef = new Workspace();
//        sef.setWorkspaceTitle("SEF");
//
//        Workspace enterprise = new Workspace();
//        enterprise.setWorkspaceTitle("Enterprise");
//
//        nanh.getWorkspaces().add(sef);
//        nanh.getWorkspaces().add(enterprise);
//
//        userRepository.save(nanh);
    }
}
