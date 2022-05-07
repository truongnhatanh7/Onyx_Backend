package com.rmit.onyx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long userId;
    private String name;
    private String username;
    private String password;
    private String avatarURL;
    private List<WorkspaceDTO> workspaces = new ArrayList<>();

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.avatarURL = user.getAvatarURL();

        for (Workspace workspace : user.getWorkspaces()) {
            WorkspaceDTO temp = new WorkspaceDTO();
            temp.setWorkspaceTitle(workspace.getWorkspaceTitle());
            temp.setWorkspaceId(workspace.getWorkspaceId());
            workspaces.add(temp);
        }
    }
}
