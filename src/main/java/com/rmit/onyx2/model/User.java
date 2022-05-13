package com.rmit.onyx2.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name")
    private String name;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "avatarURL")
    private String avatarURL = "";

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_workspaces",
        joinColumns = {
                @JoinColumn(name = "user_id")
        },
        inverseJoinColumns = {
                @JoinColumn(name = "workspace_id")
        }
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Workspace> workspaces = new ArrayList<>();

    public void addWorkspace(Workspace workspace) {
        this.workspaces.add(workspace);
        workspace.getUsers().add(this);
    }

}
