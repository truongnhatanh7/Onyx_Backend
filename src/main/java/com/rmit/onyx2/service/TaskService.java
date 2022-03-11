package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private WorkspaceListRepository workspaceListRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, WorkspaceListRepository workspaceListRepository) {
        this.taskRepository = taskRepository;
        this.workspaceListRepository = workspaceListRepository;
    }

    public List<Task> getAllTasksByListId(Long listId) {
        Optional<WorkspaceList> list = workspaceListRepository.findById(listId);
        if (list.isPresent()) {
            return list.get().getTasks();
        }
        return null;
    }


    public ResponseEntity<Task> addTaskByListId(Long listId, Task task) {
        Optional<WorkspaceList> list = workspaceListRepository.findById(listId);
        if (list.isPresent()) {
//            list.get().getTasks().add(task);
            task.setWorkspaceList(list.get());
//            workspaceListRepository.save(list.get());
            taskRepository.save(task);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<Task> deleteTaskById(Long taskId, Long listId) {
        //Find the list id
        Optional<WorkspaceList> list = workspaceListRepository.findById(listId);
        if(list.isPresent()) {
            list.get().getTasks().remove(taskId);
            return ResponseEntity.ok().build();
        }
       return ResponseEntity.badRequest().build();
    }
}
