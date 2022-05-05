package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.UserDTO;
import com.rmit.onyx2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all-users/")
//    @Operation(
//            summary = "Get All user",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Return a list of  user",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = UserDTO.class))),
//                    @ApiResponse(responseCode = "400", description = "null",
//                            content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ObjectUtils.Null.class))
//                    )}
//    )
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find-by-id/{userId}")
    public UserDTO getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    public UserDTO addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        return userService.editUser(user);
    }

    @PatchMapping("/edit-password/{userId}/{newPassword}")
    public void editPassword(@PathVariable(name = "userId") Long userId, @PathVariable(name = "newPassword") String newPassword) {
        userService.editPassword(userId, newPassword);
    }

    @PatchMapping("/edit-username/{userId}/{newUsername}")
    public void editUsername(@PathVariable(name = "userId") Long userId, @PathVariable(name = "newUsername") String newUsername) {
        userService.editUsername(userId, newUsername);
    }

    @PostMapping("/add-workspace-for-user-by-id/{workspaceId}/{userId}")
    public ResponseEntity<User> addWorkspaceForUserById(@PathVariable(name = "workspaceId") Long workspaceId, @PathVariable(name = "userId") Long userId) {
        return userService.addWorkspaceForUserById(workspaceId, userId);
    }

    @DeleteMapping("/remove-user-from-workspace/{workspaceId}/{userId}")
    public void removeUserFromWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId, @PathVariable(name = "userId") Long userId) {
        userService.removeUserFromWorkspaceById(workspaceId, userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUserById(@PathVariable(name = "userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
