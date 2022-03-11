package com.rmit.onyx2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
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


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH},fetch = FetchType.LAZY)
    @JoinTable(
            name = "users_workspaces",
            joinColumns =
                    @JoinColumn(name = "workspace_id")
            ,
            inverseJoinColumns =
                    @JoinColumn(name = "user_id")
            )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "workspace", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private Set<WorkspaceList> workspaceLists = new HashSet<>();

    public void removeUser(User user) {
        users.remove(user);
        user.getWorkspaces().remove(this);
    }
}
