package com.rmit.onyx2;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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
