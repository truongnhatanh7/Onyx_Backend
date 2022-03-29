package com.rmit.onyx2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkspaceListDTO {
    private Long listId;
    private String name;
    private WorkspaceDTO workspace;

    public WorkspaceListDTO(WorkspaceList workspaceList) {
        this.listId = workspaceList.getListId();
        this.name = workspaceList.getName();
        this.workspace = new WorkspaceDTO(workspaceList.getWorkspace());
    }

}
