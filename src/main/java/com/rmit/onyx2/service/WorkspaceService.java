package com.rmit.onyx2.service;

import com.rmit.onyx2.model.*;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.UserRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.text.html.Option;
import java.util.*;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final WorkspaceListRepository workspaceListRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public WorkspaceService(UserRepository userRepository,
                            WorkspaceRepository workspaceRepository,
                            WorkspaceListRepository workspaceListRepository,
                            TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceListRepository = workspaceListRepository;
        this.taskRepository = taskRepository;
    }

    public List<WorkspaceDTO> getAllWorkspaces() {
        List<Workspace> temp = workspaceRepository.findAll();
        List<WorkspaceDTO> getResult = new ArrayList<>();
        for (Workspace workspace : temp) {
            getResult.add(new WorkspaceDTO(workspace));
        }
        return getResult;
    }

    public WorkspaceDTO addWorkspace(Workspace workspace) {
        return new WorkspaceDTO(workspaceRepository.saveAndFlush(workspace));

    }

    public void deleteWorkspaceById(Long workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isPresent()) {
            for (User user : userRepository.findAll()) {
                user.getWorkspaces().removeIf(w -> w.getWorkspaceId().equals(workspaceId));
            }

            workspace.get().getUsers().clear();


            for (WorkspaceList workspaceList : workspace.get().getWorkspaceLists()) {
                for (Task task : workspaceList.getTasks()) {
                    taskRepository.deleteById(task.getTaskId());
                }
                workspaceListRepository.deleteById(workspaceList.getListId());
            }

            workspace.get().getWorkspaceLists().clear();
            workspaceRepository.deleteById(workspaceId);

        }

    }

    @Transactional
    public ResponseEntity<Workspace> editWorkspace(Workspace workSpace) {
        String hsql = "update Workspace w set w.workspaceTitle =: title where w.workspaceId =: id";
        Query query = entityManager.createQuery(hsql);
        query.setParameter("title",workSpace.getWorkspaceTitle());
        query.setParameter("id",workSpace.getWorkspaceId());
        entityManager.flush();
        int result = query.executeUpdate();
        if (result != 0) {
            return ResponseEntity.ok().build();
        }
        return  ResponseEntity.badRequest().build();
    }


    public List<WorkspaceDTO> getWorkspacesByUserId(Long userId) {
        List<WorkspaceDTO> workspaceDTOS = new ArrayList<>();

        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            for (Workspace workspace : user.get().getWorkspaces()) {
                workspaceDTOS.add(new WorkspaceDTO(workspace));
            }
        }

        return workspaceDTOS;
    }

    public WorkspaceDTO getWorkspaceById(Long workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        return workspace.map(WorkspaceDTO::new).orElseGet(WorkspaceDTO::new);
    }
}
