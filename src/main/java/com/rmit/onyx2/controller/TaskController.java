package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/task")
public class TaskController {

    private TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{listId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public List<Task> getAllTasksByListId(@PathVariable(name = "listId") Long listId) {
        return taskService.getAllTasksByListId(listId);
    }

    @PostMapping("/{listId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public ResponseEntity<Task> addTaskByListId(@PathVariable(name = "listId") Long listId, @RequestBody Task task) {
        return taskService.addTaskByListId(listId, task);
    }

    @DeleteMapping("/{taskId}")
    @CrossOrigin(origins = "http://127.0.0.1:5500/")
    public void deleteTaskById(@PathVariable(name = "taskId") Long taskId) {

        taskService.deleteTaskById(taskId);
    }

}
