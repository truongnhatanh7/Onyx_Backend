package com.rmit.onyx2.service;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceDTO;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

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

//TODO: Delete workspace
    public ResponseEntity<Workspace> deletWorkspaceById(long workspaceId){
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if(workspace.isPresent()) {
//            Workspace tmpWorkspace = workspace.get();
//            //Loop through workspace list of work spaces to remove workspace
//            for (User user : tmpWorkspace.getUsers()) {
//                if (user.getWorkspaces().contains(tmpWorkspace)) {
//                    user.removeWorkSpace(tmpWorkspace);
//                }
//            }
//            workspaceRepository.save(tmpWorkspace);
            workspaceRepository.delete(workspace.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
