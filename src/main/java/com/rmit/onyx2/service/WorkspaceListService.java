package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class WorkspaceListService {

    private WorkspaceListRepository workspaceListRepository;
    private WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceListService(WorkspaceListRepository workspaceListRepository, WorkspaceRepository workspaceRepository) {
        this.workspaceListRepository = workspaceListRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public List<WorkspaceList> getWorkspaceListByWorkspaceId(Long id) {
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
}
