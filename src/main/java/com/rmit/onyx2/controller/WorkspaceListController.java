package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.model.WorkspaceListDTO;
import com.rmit.onyx2.service.WorkspaceListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/list")
public class WorkspaceListController {

    private WorkspaceListService workspaceListService;

    @Autowired
    public WorkspaceListController(WorkspaceListService workspaceListService) {
        this.workspaceListService = workspaceListService;
    }

    @GetMapping("/{workspaceId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public Set<WorkspaceList> getWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceListService.getWorkspaceListByWorkspaceId(workspaceId);
    }

    @PostMapping("/{workspaceId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    @ResponseBody
    public WorkspaceListDTO addWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId, @RequestBody WorkspaceList workspaceList) {
        return workspaceListService.addWorkspaceListByWorkspaceId(workspaceId, workspaceList);
    }

    @PutMapping("/{workspaceId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500")
    public Integer editWorkspaceList(@RequestBody WorkspaceList workspaceList,@PathVariable(name = "workspaceId") Long workspaceId){
        return workspaceListService.editWorkspaceList(workspaceList,workspaceId);
    }

    @DeleteMapping("/{workspaceListId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public void deleteWorkspaceListById(@PathVariable(name = "workspaceListId") Long workspaceListId) {
        workspaceListService.deleteWorkspaceListById(workspaceListId);
    }
}
