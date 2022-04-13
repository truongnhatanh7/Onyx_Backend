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
    private WorkspaceListService workspaceListService;

    @Autowired
    public WorkspaceListController(WorkspaceListService workspaceListService) {
        this.workspaceListService = workspaceListService;
    }

    @GetMapping("/{workspaceId}")
//    @Operation(
//            summary = "Get workspace list by workspace id",
//            responses = {
//                    @ApiResponse(responseCode  = "200",description = "Return a list of workspace",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = WorkspaceList.class))),
//                    @ApiResponse(responseCode = "400", description = "Bad request",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ObjectUtils.Null.class))
//                    )
//            }
//    )
    public List<WorkspaceList> getWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId) {
        return workspaceListService.getWorkspaceListByWorkspaceId(workspaceId);
    }

    @PostMapping("/{workspaceId}")
//    @Operation(
//    summary = "Add workspace list into workspace by workspace id",
//    responses = {
//        @ApiResponse(responseCode = "200",description = "Return WorkspaceListDTO",
//                content = @Content(mediaType = "application/json",
//                        schema = @Schema(implementation =WorkspaceListDTO.class))),
//        @ApiResponse(responseCode = "400", description = "Bad Request",
//                content = @Content(mediaType = "application/json",
//                schema = @Schema(implementation = ObjectUtils.Null.class)
//                ))}
//    )
    @ResponseBody
    public WorkspaceListDTO addWorkspaceListByWorkspaceId(@PathVariable(name = "workspaceId") Long workspaceId,
                                                          @org.springframework.web.bind.annotation.RequestBody WorkspaceList workspaceList) {
        return workspaceListService.addWorkspaceListByWorkspaceId(workspaceId, workspaceList);
    }

    @PutMapping("/{workspaceId}")
//    @Operation(
//            summary = "Edit workspace list to workspace",
//            responses = {
//                    @ApiResponse(responseCode = "200",description = "Success",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation =Integer.class))),
//                    @ApiResponse(responseCode = "400", description = "Bad request",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ObjectUtils.Null.class)
//                            ))}
//    )
    public Integer editWorkspaceList(@RequestBody WorkspaceList workspaceList, @PathVariable(name = "workspaceId") Long workspaceId){
        return workspaceListService.editWorkspaceList(workspaceList,workspaceId);
    }

    @DeleteMapping("/{workspaceListId}")
//    @Operation(
//            summary = "Delete workspace list by ID",
//            responses = {
//                    @ApiResponse(responseCode = "200",description = "Success",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation =ObjectUtils.Null.class))),
//                    @ApiResponse(responseCode = "400", description = "Bad request",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ObjectUtils.Null.class)
//                            ))}
//    )
    public void deleteWorkspaceListById(@PathVariable(name = "workspaceListId") Long workspaceListId) {
        workspaceListService.deleteWorkspaceListById(workspaceListId);
    }
}
