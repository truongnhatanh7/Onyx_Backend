package com.rmit.onyx2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "workspace_list")
public class WorkspaceList {
    @Id
    @GeneratedValue
    @Column(name = "list_id")
    private Long listId;
    @Column(name = "name")
    private String name;

    //Workspace can have many workspacelist
    @ManyToOne(cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Workspace workspace;

    //Workspace List can belong to only one workspace
    @OneToMany(mappedBy = "workspaceList",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Task> tasks = new HashSet<>();
}
