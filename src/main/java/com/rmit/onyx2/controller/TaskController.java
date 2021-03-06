package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/task")
@CrossOrigin(origins = "*")
public class TaskController {
    private final TaskService taskService;

    //Dependency injection for task service
    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    //CRUD for list operation
    @GetMapping("/{listId}")
    public Set<Task> getAllTasksByListId(@PathVariable(name = "listId") Long listId) {
        return taskService.getAllTasksByListId(listId);
    }

    @PostMapping("/{listId}")
    public Task addTaskByListId(@PathVariable(name = "listId") Long listId, @org.springframework.web.bind.annotation.RequestBody Task task) {
        return taskService.addTaskByListId(listId, task);
    }

    //CRUD for operation on Task object
    @PutMapping("")
    public ResponseEntity<Task> editTask(@org.springframework.web.bind.annotation.RequestBody Task task) {
        return taskService.editTask(task);
    }

    //To change list destination
    @PutMapping("/switchList/{destinationListId}")
    public ResponseEntity<Task> editTask(@RequestBody Task task, @PathVariable(name = "destinationListId") Long destinationListId) {
        return taskService.editTask(task, destinationListId);
    }

    @PatchMapping("/setPos/{taskId}/{pos}")
    public void setPos(@PathVariable Long taskId, @PathVariable Integer pos) {
        taskService.setPos(taskId, pos);
    }

    //Setting description for task
    @PatchMapping("/setDesc/{taskId}")
    public void setDesc(@PathVariable Long taskId, @RequestBody String description) {
        taskService.setDesc(taskId, description);
    }

    //Set task deadline
    @PatchMapping("/setDeadline/{taskId}")
    public void setDeadline(@PathVariable Long taskId, @RequestParam("time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate deadline) {
        taskService.setDeadline(taskId, deadline);
    }

    //Mark a task as priority
    @PatchMapping("/setPriority/{taskId}/{priority}")
    public void setPriority(@PathVariable Long taskId, @PathVariable Integer priority) {
        taskService.setPriority(taskId, priority);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTaskById(@PathVariable(name = "taskId") Long taskId) {
        taskService.deleteTaskById(taskId);
    }
}
