package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final WorkspaceListRepository workspaceListRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TaskService(TaskRepository taskRepository, WorkspaceListRepository workspaceListRepository) {
        this.taskRepository = taskRepository;
        this.workspaceListRepository = workspaceListRepository;
    }

    public Set<Task> getAllTasksByListId(Long listId) {
        Optional<WorkspaceList> list = workspaceListRepository.findById(listId);
        return list.map(WorkspaceList::getTasks).orElse(null);
    }

    @Transactional
    public ResponseEntity<Task> editTask (Task task) {
        String hsql = "update Task t set t.taskContent =:content " +
                        "where t.taskId =: taskId ";
        Query query = entityManager.createQuery(hsql);
        query.setParameter("content",task.getTaskContent());
        query.setParameter("taskId",task.getTaskId());
        entityManager.flush();
        Integer result = query.executeUpdate();
        entityManager.clear();
        if(result == 1) {
            return  ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //In case of switching list
    @Transactional
    public ResponseEntity<Task> editTask (Task task, Long destinationListId) {
        String hsql;
        Query query;
        if(task.getTaskContent() ==null) {
            hsql = "update Task t set t.workspaceList.listId =:destinationListId " +
                    "where t.taskId =: taskId ";
            query = entityManager.createQuery(hsql);
        } else{
            hsql = "update Task t set t.taskContent =:content, t.workspaceList.listId =: destinationListId " +
                    "where t.taskId =: taskId ";
            query = entityManager.createQuery(hsql);
            query.setParameter("content",task.getTaskContent());
        }
        query.setParameter("destinationListId",destinationListId);
        query.setParameter("taskId",task.getTaskId());
        entityManager.flush();
        Integer result = query.executeUpdate();
        entityManager.clear();
        if(result == 1) {
            return  ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    public Task addTaskByListId(Long listId, Task task) {
        Optional<WorkspaceList> list = workspaceListRepository.findById(listId);
        if (list.isPresent()) {
            task.setWorkspaceList(list.get());
            taskRepository.save(task);
            List<Task> temp =  taskRepository.findAll();
            if (temp.size() > 0) {
                return temp.get(temp.size() - 1);
            } else {
                return new Task();
            }
        }
        return new Task();
    }

    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Transactional
    @Async
    public void setPos(Long taskId, Integer pos) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.updatePos(taskId, pos);

        }
    }

    @Transactional
    public void setPriority(Long taskId, Integer priority) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.updatePriority(taskId, priority);
        }
    }
}
