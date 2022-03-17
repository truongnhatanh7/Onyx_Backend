package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.UserDTO;
import com.rmit.onyx2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "https://onyx-main.herokuapp.com")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all-users/")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find-by-id/{userId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public UserDTO getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        return userService.editUser(user);
    }


    @PostMapping("/add-workspace-for-user-by-id/{workspaceId}/{userId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public ResponseEntity<User> addWorkspaceForUserById(@PathVariable(name = "workspaceId") Long workspaceId, @PathVariable(name = "userId") Long userId) {
        return userService.addWorkspaceForUserById(workspaceId, userId);
    }

    @DeleteMapping("/{userId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public void deleteUserById(@PathVariable(name = "userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
