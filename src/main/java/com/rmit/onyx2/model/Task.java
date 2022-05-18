package com.rmit.onyx2.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

//Class model for task
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long taskId;

    @Column(name = "task_content")
    private String taskContent;

    @Column(name = "pos")
    private Integer pos = 0;

    @Column(name = "priority")
    private Integer priority = 0;

    @Column(name = "description")
    private String description = "";

    @Column(name = "deadline")
    private LocalDate deadline;

    //Workspace list can have many task
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "list_id", referencedColumnName = "list_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private WorkspaceList workspaceList;

}
