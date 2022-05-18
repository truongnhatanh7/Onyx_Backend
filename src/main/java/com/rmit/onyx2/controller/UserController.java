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
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    //Dependency injection of User Service
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // CRUD methods
    //CRUD Relating to user
    //Read
    @GetMapping("/all-users/")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find-by-id/{userId}")
    public UserDTO getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    //Create
    @PostMapping
    public UserDTO addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    //Update
    @PutMapping("")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        return userService.editUser(user);
    }

    @PatchMapping("/edit-name/{userId}/{newName}")
    public void editName(@PathVariable(name = "userId") Long userId, @PathVariable(name = "newName") String newName) {
        userService.editName(userId, newName);
    }

    @PatchMapping("/edit-password/{userId}/{newPassword}")
    public void editPassword(@PathVariable(name = "userId") Long userId, @PathVariable(name = "newPassword") String newPassword) {
        userService.editPassword(userId, newPassword);
    }

    @PatchMapping("/edit-username/{userId}/{newUsername}")
    public void editUsername(@PathVariable(name = "userId") Long userId, @PathVariable(name = "newUsername") String newUsername) {
        userService.editUsername(userId, newUsername);
    }

    //Function allowing user to edit the avatar
    @PatchMapping(path = "/edit-avatar")
    public void editAvatarURL(@RequestParam(name = "userId") Long userId, @RequestParam(name = "avatar") String newAvatarURL) {
        userService.editAvatarURL(userId, newAvatarURL);
    }

    //Delete
    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable(name = "userId") Long userId) {
        userService.deleteUserById(userId);
    }

    //CRUD Method responsible for adding and removing workspace of the user.
    @PostMapping("/add-workspace-for-user-by-id/{workspaceId}/{userId}")
    public ResponseEntity<User> addWorkspaceForUserById(@PathVariable(name = "workspaceId") Long workspaceId, @PathVariable(name = "userId") Long userId) {
        return userService.addWorkspaceForUserById(workspaceId, userId);
    }

    @DeleteMapping("/remove-user-from-workspace/{workspaceId}/{userId}")
    public void removeUserFromWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId, @PathVariable(name = "userId") Long userId) {
        userService.removeUserFromWorkspaceById(workspaceId, userId);
    }

}
