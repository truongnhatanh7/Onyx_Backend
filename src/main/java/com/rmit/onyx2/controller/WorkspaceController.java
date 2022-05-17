package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceDTO;
import com.rmit.onyx2.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/workspace")
@CrossOrigin(origins = "*")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    //Injecting WorkspaceService dependency to Workspace Controller
    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    //Work space CRUD methods
    //Read
    @GetMapping
    public List<WorkspaceDTO> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping("/get-workspace/{workspaceId}")
    public WorkspaceDTO getWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceService.getWorkspaceById(workspaceId);
    }

    @GetMapping("/get-workspace-by-user-id/{userId}")
    public List<WorkspaceDTO> getWorkspaceByUserId(@PathVariable(name = "userId") Long userId) {
        return workspaceService.getWorkspacesByUserId(userId);
    }

    //Add
    @PostMapping
    public WorkspaceDTO addWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.addWorkspace(workspace);
    }

    //Update
    @PutMapping("")
    public ResponseEntity<Workspace> editWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.editWorkspace(workspace);
    }

    //Delete
    @DeleteMapping("/delete-workspace/{workspaceId}")
    public void deleteWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        workspaceService.deleteWorkspaceById(workspaceId);
    }

    //Need a controller to transfer owner of the wirkspace
    @PatchMapping("/edit-owner/{workspaceId}/{userId}")
    public void editOwner(@PathVariable(name = "workspaceId") Long workspaceId,@PathVariable(name = "userId") Long userId) {
        workspaceService.editOwner(workspaceId, userId);
    }

    //Need a method to get owner id of workspace based on workspace Id
    @GetMapping("/get-owner/{workspaceId}")
    public Long getOwnerId(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceService.getOwnerId(workspaceId);
    }



}
