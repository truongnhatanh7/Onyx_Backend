package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceDTO;
import com.rmit.onyx2.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/workspace")
@CrossOrigin(origins = "http://127.0.0.1:5501")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public List<WorkspaceDTO> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping("/get-workspace/{workspaceId}")
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public WorkspaceDTO getWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceService.getWorkspaceById(workspaceId);
    }

    @GetMapping("/get-workspace-by-user-id/{userId}")
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public List<WorkspaceDTO> getWorkspaceByUserId(@PathVariable(name = "userId") Long userId) {
        return workspaceService.getWorkspacesByUserId(userId);
    }

    @PostMapping
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public WorkspaceDTO addWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.addWorkspace(workspace);
    }

    @PutMapping("")
    @CrossOrigin(origins = "http://127.0.0.1:5501")
    public ResponseEntity<Workspace> editWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.editWorkspace(workspace);
    }

    @DeleteMapping("/delete-workspace/{workspaceId}")
    @CrossOrigin(origins = "http://127.0.0.1:5501/")
    public void deleteWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        workspaceService.deleteWorkspaceById(workspaceId);
    }
}
