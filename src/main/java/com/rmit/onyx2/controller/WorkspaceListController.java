package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.model.WorkspaceListDTO;
import com.rmit.onyx2.service.WorkspaceListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/list")
@CrossOrigin(origins = "*")
public class WorkspaceListController {
    private final WorkspaceListService workspaceListService;

    @Autowired
    public WorkspaceListController(WorkspaceListService workspaceListService) {
        this.workspaceListService = workspaceListService;
    }

    @GetMapping("/{workspaceId}")
    public List<WorkspaceList> getWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceListService.getWorkspaceListByWorkspaceId(workspaceId);
    }

    @PostMapping("/{workspaceId}")
    public WorkspaceListDTO addWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId,
                                                          @RequestBody WorkspaceList workspaceList) {
        return workspaceListService.addWorkspaceListByWorkspaceId(workspaceId, workspaceList);
    }

    @PutMapping("/{workspaceId}")
    public Integer editWorkspaceList(@RequestBody WorkspaceList workspaceList, @PathVariable(name = "workspaceId") Long workspaceId){
        return workspaceListService.editWorkspaceList(workspaceList,workspaceId);
    }

    @DeleteMapping("/{workspaceListId}")
    public void deleteWorkspaceListById(@PathVariable(name = "workspaceListId") Long workspaceListId) {
        workspaceListService.deleteWorkspaceListById(workspaceListId);
    }
}
