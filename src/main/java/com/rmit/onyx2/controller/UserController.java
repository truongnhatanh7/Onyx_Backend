package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.UserDTO;
import com.rmit.onyx2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Get All user",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Return a list of  user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "null")}
    )
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/find-by-id/{userId}")
    @Operation(
            summary = "Get user by id",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Return user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDTO.class))),
                    @ApiResponse(responseCode = "400", description = "null")}
    )
    public UserDTO getUserById(@PathVariable(name = "userId") Long userId) {
        return userService.getUserById(userId);
    }

    @PostMapping
    @Operation(
            summary = "Add user",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Return user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "400", description = "null")}
    )
    public ResponseEntity<User> addUser(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Pet object that needs to be added to the store",
            required = true) User user) {
        return userService.addUser(user);
    }

    @PutMapping("")
    @Operation(
            summary = "Edit user",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Finish editing user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Failed to edit user")}
    )
    public ResponseEntity<User> editUser(@RequestBody(
            description = "Edit the existing user",
            required = true,
            content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class)
            )
    ) User user) {
        return userService.editUser(user);
    }


    @PostMapping("/add-workspace-for-user-by-id/{workspaceId}/{userId}")
    @Operation(
            summary = "Add work space for user by id",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Finish adding workspace for user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Failed to edit user")}
    )
    public ResponseEntity<User> addWorkspaceForUserById(@PathVariable(name = "workspaceId", value = "workspaceId") Long workspaceId, @PathVariable(name = "userId", value = "userId") Long userId) {
        return userService.addWorkspaceForUserById(workspaceId, userId);
    }

    @DeleteMapping("/remove-user-from-workspace/{workspaceId}/{userId}")
    @Operation(
            summary = "Remove user from workspace",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Finish removing user from workspace",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Failed to edit user")}
    )
    public ResponseEntity<User> removeUserFromWorkspaceById( @PathVariable(name = "workspaceId", value = "workspaceId") Long workspaceId, @PathVariable(name = "userId", value = "userId") Long userId) {
        return userService.removeUserFromWorkspaceById(workspaceId, userId);
    }

    @DeleteMapping("/{userId}")
    @Operation(
            summary = "Delete user from workspace",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Finish removing user from workspace",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "400", description = "Failed to edit user")}
    )
    public void deleteUserById(@PathVariable(value ="userid", name = "userId") Long userId) {
        userService.deleteUserById(userId);
    }
}
