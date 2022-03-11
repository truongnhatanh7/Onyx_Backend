package com.rmit.onyx2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private List<WorkspaceList> workspaceLists = new ArrayList<>();

}
