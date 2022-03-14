package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private TaskRepository taskRepository;
    private WorkspaceListRepository workspaceListRepository;
    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public ResponseEntity<Task> editTask (Task task) {
        if(task.getTaskContent() ==null) {

        }
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

    //Incase of switching list
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

    public void deleteTaskById(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
