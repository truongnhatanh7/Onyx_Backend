package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceDTO;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkspaceService {

    private WorkspaceRepository workspaceRepository;

    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
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

    public void deleteWorkSpaceById(long workspaceId) {
        workspaceRepository.deleteById(workspaceId);
    }
}
