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

import java.util.List;

@RestController
@RequestMapping("/api/v1/workspace")
@CrossOrigin(origins = "*")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Autowired
    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    @Operation(
            summary = "Get all of the workspace",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return a list of workspace",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Workspace.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ObjectUtils.Null.class))
                    )}
    )
    public List<WorkspaceDTO> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping("/get-workspace/{workspaceId}")
    @Operation(
            summary = "Get workspace by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return a workspace with valid Id",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Workspace.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ObjectUtils.Null.class))
                    )}
    )
    public Workspace getWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceService.getWorkspace(workspaceId);
    }

    @GetMapping("/get-workspace-by-user-id/{userId}")
    @Operation(
            summary = "Get all of workspace by userId",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return a list of workspace",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Workspace.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ObjectUtils.Null.class))
                    )}
    )
    public List<WorkspaceDTO> getWorkspaceByUserId(@PathVariable(name = "userId") Long userId) {
        return workspaceService.getWorkspacesByUserId(userId);
    }

    @PostMapping
    @Operation(
            summary = "Add workspace",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Return added workspace in DB",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = WorkspaceDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ObjectUtils.Null.class))
                    )}
    )
    public WorkspaceDTO addWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.addWorkspace(workspace);
    }

    @PutMapping("")
    @Operation(
            summary = "Edit workspace information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully edit workspace",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ResponseEntity.class))
                    )}
    )
    public ResponseEntity<Workspace> editWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.editWorkspace(workspace);
    }

    @DeleteMapping("/delete-workspace/{workspaceId}")
    @Operation(
            summary = "Delete workspace by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully edit workspace",
                            content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ObjectUtils.Null.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = ObjectUtils.Null.class))
                    )}
    )
    public void deleteWorkspaceById(@PathVariable(name = "workspaceId") Long workspaceId) {
        workspaceService.deleteWorkspaceById(workspaceId);
    }
}
