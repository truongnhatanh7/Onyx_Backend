package com.rmit.onyx2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private Set<Workspace> workspaces = new HashSet<>();

    public void addWorkspace(Workspace workspace) {
        this.workspaces.add(workspace);
        workspace.getUsers().add(this);
    }

}
