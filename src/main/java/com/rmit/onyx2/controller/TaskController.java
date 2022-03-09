package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{listId}")
    public List<Task> getAllTasksByListId(@PathVariable(name = "listId") Long listId) {
        return taskService.getAllTasksByListId(listId);
    }

    @PostMapping("/{listId}")
    public ResponseEntity<Task> addTaskByListId(@PathVariable(name = "listId") Long listId, @RequestBody Task task) {
        return taskService.addTaskByListId(listId, task);
    }

    @DeleteMapping("/{listId}")
    public void deleteTaskByListId(@PathVariable(name = "listId") Long listId) {
        taskService.deleteTaskById(listId);
    }
}
