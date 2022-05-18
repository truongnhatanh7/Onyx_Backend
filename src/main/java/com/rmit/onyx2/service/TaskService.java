package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
public class TaskService {
    private final SseService sseService;
    private final TaskRepository taskRepository;
    private final WorkspaceListRepository workspaceListRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public TaskService(SseService sseService, TaskRepository taskRepository, WorkspaceListRepository workspaceListRepository) {
        this.sseService = sseService;
        this.taskRepository = taskRepository;
        this.workspaceListRepository = workspaceListRepository;
    }


    public Set<Task> getAllTasksByListId(Long listId) {
        Optional<WorkspaceList> list = workspaceListRepository.findById(listId);
        return list.map(WorkspaceList::getTasks).orElse(null);
    }

    @Transactional
    public ResponseEntity<Task> editTask (Task task) {
        //Edit task using Hibernate Sql Language
        String hsql = "update Task t set t.taskContent =:content " +
                        "where t.taskId =: taskId ";
        Query query = entityManager.createQuery(hsql);
        query.setParameter("content",task.getTaskContent());
        query.setParameter("taskId",task.getTaskId());
        entityManager.flush();
        int result = query.executeUpdate();
        entityManager.clear();
        try {
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("editTask");

        } catch (Exception e) {
            System.out.println("Fail");
        }
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
            //Task content is available
            hsql = "update Task t set t.taskContent =:content, t.workspaceList.listId =: destinationListId " +
                    "where t.taskId =: taskId ";
            query = entityManager.createQuery(hsql);
            query.setParameter("content",task.getTaskContent());
        }
        query.setParameter("destinationListId",destinationListId);
        query.setParameter("taskId",task.getTaskId());
        entityManager.flush();
        int result = query.executeUpdate();
        entityManager.clear();
        if(result == 1) {
            try {
                //Emitt change list event
                SseEmitter sseEmitter = new SseEmitter();
                sseService.addEmitter(sseEmitter);
                sseService.doNotify("switchList");

            } catch (Exception e) {

                System.out.println("Fail");
            }

            return  ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().build();
    }

    public Task addTaskByListId(Long listId, Task task) {
        Optional<WorkspaceList> list = workspaceListRepository.findById(listId);
        if (list.isPresent()) {
            task.setWorkspaceList(list.get());
            taskRepository.save(task);
            try {
                SseEmitter sseEmitter = new SseEmitter();
                sseService.addEmitter(sseEmitter);
                sseService.doNotify("addTask");

            } catch (Exception e) {
                System.out.println("Fail");
            }
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
        //Emitting task changing event
        try {
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("deleteTask");

        } catch (Exception e) {
            System.out.println("Fail");
        }
    }

    //Need this methode to determine the position of the task
    @Transactional
    @Async
    public void setPos(Long taskId, Integer pos) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.updatePos(taskId, pos);
        }
        try {
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("setPos");

        } catch (Exception e) {
            System.out.println("Fail");
        }

    }

    //Mark a task as priority
    @Transactional
    public void setPriority(Long taskId, Integer priority) {
        Optional<Task> task = taskRepository.findById(taskId);
        if (task.isPresent()) {
            taskRepository.updatePriority(taskId, priority);
        }
        //Broadcast changes
        try {
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("setPriority");

        } catch (Exception e) {
            System.out.println("Fail");
        }
    }

    //Adding description for task
    @Transactional
    public void setDesc(Long taskId, String description) {
        Optional<Task> task = taskRepository.findById(taskId);
        String hsql;
        Query query;
        if (task.isPresent()) {
            hsql = "update Task t set t.description =:description where t.taskId=:taskId";
            query = entityManager.createQuery(hsql);
            query.setParameter("description", description);
            query.setParameter("taskId", taskId);
            entityManager.flush();
            query.executeUpdate();
            entityManager.clear();
        }
        //Emit changes
        try {
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("setDesc");

        } catch (Exception e) {
            System.out.println("Fail");
        }
    }

    //Setting and assigning deadline for task
    @Transactional
    public void setDeadline(Long taskId, LocalDate deadline) {
        Optional<Task> task = taskRepository.findById(taskId);
        String hsql;
        Query query;
        if (task.isPresent()) {
            hsql = "update Task t set t.deadline=:deadline where t.taskId=:taskId";
            query = entityManager.createQuery(hsql);
            query.setParameter("deadline", deadline);
            query.setParameter("taskId", taskId);
            entityManager.flush();
            query.executeUpdate();
            entityManager.clear();
        }
        try {
            //Emit changes
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("setDeadline");

        } catch (Exception e) {
            System.out.println("Fail");
        }
    }
}
