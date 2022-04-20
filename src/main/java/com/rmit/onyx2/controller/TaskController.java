package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{listId}")
//    @Operation(
//            summary = "Get all task by list id",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Return a Set of all Task based on list ID",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = Task.class))),
//                    @ApiResponse(responseCode = "400", description = "Return Task",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = Task.class)))
//            }
//    )
    public Set<Task> getAllTasksByListId(@PathVariable(name = "listId") Long listId) {
        return taskService.getAllTasksByListId(listId);
    }

    @PostMapping("/{listId}")
//    @Operation(
//            summary = "Add Task by list ID",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Return Added Task",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = Task.class))),
//                    @ApiResponse(responseCode = "400", description = "invalid response",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ObjectUtils.Null.class)))}
//    )
    public Task addTaskByListId(@PathVariable(name = "listId") Long listId, @org.springframework.web.bind.annotation.RequestBody Task task) {
        return taskService.addTaskByListId(listId, task);
    }

    @PutMapping("")
//    @Operation(
//            summary = "Edit task",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Edit task success fully",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ResponseEntity.class))),
//                    @ApiResponse(responseCode = "400", description = "invalid response",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ResponseEntity.class)))}
//    )
    public ResponseEntity<Task> editTask(@org.springframework.web.bind.annotation.RequestBody Task task) {
        return taskService.editTask(task);
    }

    @PatchMapping("/setPos/{taskId}/{pos}")
    public void setPos(@PathVariable Long taskId, @PathVariable Integer pos) {
        taskService.setPos(taskId, pos);
    }

    @PatchMapping("/setDesc/{taskId}")
    public void setDesc(@PathVariable Long taskId, @RequestBody String description) {
        taskService.setDesc(taskId, description);
    }

    @PatchMapping("/setDeadline/{taskId}")
    public void setDeadline(@PathVariable Long taskId, @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate deadline) {
        taskService.setDeadline(taskId, deadline);
    }

    @PatchMapping("/setPriority/{taskId}/{priority}")
    public void setPriority(@PathVariable Long taskId, @PathVariable Integer priority) { taskService.setPriority(taskId, priority); }
    //To change list destination
    @PutMapping("/switchList/{destinationListId}")
//    @Operation(
//            summary = "Change the list destination",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Edit task successfully",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ResponseEntity.class))),
//                    @ApiResponse(responseCode = "400", description = "Edit task failed",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ResponseEntity.class)))
//            }
//    )
    public ResponseEntity<Task> editTask(@RequestBody Task task, @PathVariable(name = "destinationListId") Long destinationListId) {
        return taskService.editTask(task, destinationListId);
    }


    @DeleteMapping("/{taskId}")
//    @Operation(
//            summary = "Change the list destination",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Delete user",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ObjectUtils.Null.class))),
//                    @ApiResponse(responseCode = "400", description = "Delete user",
//                            content = @Content(mediaType = "application/json",
//                                    schema = @Schema(implementation = ObjectUtils.Null.class)))}
//    )
    public void deleteTaskById(@PathVariable(name = "taskId") Long taskId) {
        taskService.deleteTaskById(taskId);
    }
}
