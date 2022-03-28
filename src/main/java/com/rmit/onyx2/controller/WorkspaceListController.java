package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.model.WorkspaceListDTO;
import com.rmit.onyx2.service.WorkspaceListService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/list")
@CrossOrigin(origins = "*")
public class WorkspaceListController {
//TODO: Add documentation for this
    private WorkspaceListService workspaceListService;

    @Autowired
    public WorkspaceListController(WorkspaceListService workspaceListService) {
        this.workspaceListService = workspaceListService;
    }


    @GetMapping("/{workspaceId}")
    @Operation(
            summary = "Get work space by workspace id",
            responses = {
                    @ApiResponse(responseCode  = "200",description = "Return user",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = WorkspaceList.class))),
                    @ApiResponse(responseCode = "400", description = "null")}
    )
    public Set<WorkspaceList> getWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceListService.getWorkspaceListByWorkspaceId(workspaceId);
    }

    @PostMapping("/{workspaceId}")
    @Operation(
    summary = "Add workspace list to workspace",
    responses = {
        @ApiResponse(description = "The user",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = User.class))),
        @ApiResponse(responseCode = "400", description = "User not found")}
    )
    @ResponseBody
    public ResponseEntity<WorkspaceList> addWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId, @RequestBody WorkspaceList workspaceList) {
        return workspaceListService.addWorkspaceListByWorkspaceId(workspaceId, workspaceList);
    }

    @PutMapping("/{workspaceId}")
    public Integer editWorkspaceList(@RequestBody WorkspaceList workspaceList,@PathVariable(name = "workspaceId") Long workspaceId){
        return workspaceListService.editWorkspaceList(workspaceList,workspaceId);
    }

    @DeleteMapping("/{workspaceListId}")
    public void deleteWorkspaceListById(@PathVariable(name = "workspaceListId") Long workspaceListId) {
        workspaceListService.deleteWorkspaceListById(workspaceListId);
    }
}
