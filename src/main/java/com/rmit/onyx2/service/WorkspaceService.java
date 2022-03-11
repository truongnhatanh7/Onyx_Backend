package com.rmit.onyx2.service;

import com.rmit.onyx2.model.*;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.UserRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.hibernate.jdbc.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final WorkspaceListRepository workspaceListRepository;

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

    public void addWorkspace(Workspace workspace) {
        workspaceRepository.saveAndFlush(workspace);
    }

    public void deleteWorkspaceById(Long workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isPresent()) {
            for (WorkspaceList workspaceList : workspace.get().getWorkspaceLists()) {
                for (Task task : workspaceList.getTasks()) {
                    taskRepository.deleteById(task.getTaskId());
                }
                workspaceListRepository.deleteById(workspaceList.getListId());
            }

            for (User user : userRepository.findAll()) {
                user.getWorkspaces().stream()
                        .filter(w -> w.getWorkspaceId().equals(workspaceId))
                        .forEach(w -> user.getWorkspaces().remove(w));
            }

            workspaceRepository.deleteById(workspaceId);

        }

    }

    public List<WorkspaceDTO> getWorkspacesByUserId(Long userId) {
        List<Workspace> workspaces = workspaceRepository.findAllByUserId(userId);
        List<WorkspaceDTO> workspaceDTOS = new ArrayList<>();
        for (Workspace workspace : workspaces) {
            workspaceDTOS.add(new WorkspaceDTO(workspace));
        }
        return workspaceDTOS;
    }
}
