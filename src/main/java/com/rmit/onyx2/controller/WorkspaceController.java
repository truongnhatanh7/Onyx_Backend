package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceDTO;
import com.rmit.onyx2.service.WorkspaceService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/workspace")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public List<WorkspaceDTO> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping("/get-workspace-by-user-id/{userId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public List<WorkspaceDTO> getWorkspaceByUserId(Long userId) {
        return workspaceService.getWorkspacesByUserId(userId);
    }

    @PostMapping
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public void addWorkspace(@RequestBody Workspace workspace) {
        workspaceService.addWorkspace(workspace);
    }

    @DeleteMapping("/{workspaceId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public void deleteWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        workspaceService.deleteWorkspaceById(workspaceId);
    }
}
