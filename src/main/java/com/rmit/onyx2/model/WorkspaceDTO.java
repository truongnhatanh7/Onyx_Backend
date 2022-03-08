package com.rmit.onyx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceDTO {
    private Long workspaceId;
    private String workspaceTitle;
    List<WorkspaceListDTO> workspaceLists = new ArrayList<>();
    List<UserDTO> users = new ArrayList<>();

    public WorkspaceDTO(Workspace workspace) {
        this.workspaceId = workspace.getWorkspaceId();
        this.workspaceTitle = workspace.getWorkspaceTitle();

        for (WorkspaceList workspaceList : workspace.getWorkspaceLists()) {
            WorkspaceListDTO workspaceListDTO = new WorkspaceListDTO();
            workspaceListDTO.setListId(workspaceList.getListId());
            workspaceListDTO.setName(workspaceList.getName());
            workspaceLists.add(workspaceListDTO);
        }
        for (User user : workspace.getUsers()) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUserId(user.getUserId());
            userDTO.setName(user.getName());
            userDTO.setUsername(user.getUsername());
            userDTO.setPassword(user.getPassword());
            users.add(userDTO);
        }
    }
}
