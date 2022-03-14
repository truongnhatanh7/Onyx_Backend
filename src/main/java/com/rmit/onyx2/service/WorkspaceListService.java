package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class WorkspaceListService {

    private WorkspaceListRepository workspaceListRepository;
    private WorkspaceRepository workspaceRepository;
    private TaskRepository taskRepository;

    @Autowired
    public WorkspaceListService(WorkspaceListRepository workspaceListRepository, WorkspaceRepository workspaceRepository, TaskRepository taskRepository) {
        this.workspaceListRepository = workspaceListRepository;
        this.workspaceRepository = workspaceRepository;
        this.taskRepository = taskRepository;
    }

    public Set<WorkspaceList> getWorkspaceListByWorkspaceId(Long id) {
        Optional<Workspace> workspace = workspaceRepository.findById(id);
        if (workspace.isPresent()) {
            return workspace.get().getWorkspaceLists();
        }
        return null;
    }

    public ResponseEntity<WorkspaceList> addWorkspaceListByWorkspaceId(Long workspaceId, WorkspaceList workspaceList) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isPresent()) {
            workspace.get().getWorkspaceLists().add(workspaceList);
            workspaceList.setWorkspace(workspace.get());
            workspaceRepository.save(workspace.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //A class to edit the information of workspace
    public WorkspaceList (WorkspaceList workspaceList) {
        Optional<WorkspaceList> workspaceListTmp =  workspaceListRepository.findById(workspaceList.getListId());
        if(workspaceListTmp.isPresent()) {
//            workspaceListRepository.save(workspaceList);
//            return ResponseEntity.ok().build();
            return workspaceListRepository.getById(workspaceList.getListId());
        }
//        return ResponseEntity.badRequest().build();
        return null;
    }

    public void deleteWorkspaceListById(Long workspaceListId) {
        Optional<WorkspaceList> temp = workspaceListRepository.findById(workspaceListId);
        if (temp.isPresent()) {
            for (Task t : temp.get().getTasks()) {
                taskRepository.deleteById(t.getTaskId());
            }
            workspaceListRepository.deleteById(workspaceListId);
        }
    }
}
