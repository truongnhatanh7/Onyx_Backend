package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceDTO;
import com.rmit.onyx2.service.WorkspaceService;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/workspace")
public class WorkspaceController {

    private WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    public List<WorkspaceDTO> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @PostMapping
    public void addWorkspace(@RequestBody Workspace workspace) {
        workspaceService.addWorkspace(workspace);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Workspace> deleteWorkSpaceById(@PathVariable(name="workspaceId") long workspaceId) {
       return workspaceService.deletWorkspaceById(workspaceId);
    }
}
