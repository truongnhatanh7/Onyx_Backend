package com.rmit.onyx2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "workspaces")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workspace {

    @Id
    @Column(name = "workspace_id")
    @GeneratedValue
    private Long workspaceId;

    @Column(name = "workspace_title")
    private String workspaceTitle;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "workspaces", fetch = FetchType.LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<WorkspaceList> workspaceLists = new HashSet<>();

}
