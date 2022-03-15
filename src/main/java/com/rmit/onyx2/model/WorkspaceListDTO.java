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
    private Workspace workspace;
//    private List<WorkspaceListDTO> workspaceListDTOS = new ArrayList<>();

    public WorkspaceListDTO(WorkspaceList workspaceList) {
        this.listId = workspaceList.getListId();
        this.name = workspaceList.getName();
        this.workspace = workspaceList.getWorkspace();
    }

}
