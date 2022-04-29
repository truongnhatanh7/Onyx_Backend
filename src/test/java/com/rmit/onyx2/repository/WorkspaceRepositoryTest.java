package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class WorkspaceRepositoryTest {

    @Autowired
    WorkspaceRepository workspaceRepository;

    @BeforeEach
    void setUp() {
        Set<User> userSet = new HashSet<>();
        Set<WorkspaceList> listSet = new HashSet<>();
        List<Workspace> workspaceSet = new ArrayList<>();

        Workspace workspace = new Workspace(1L, "Backend test", null, null);
        User user = new User(
                2L,
                "Tri Lai",
                "trilai",
                "123456",
                null
        );
        WorkspaceList workspaceList = new WorkspaceList(3L, "Workspace Repos test", workspace, null);

        user.setWorkspaces(workspaceSet);
        userSet.add(user);
        listSet.add(workspaceList);

        workspace.setUsers(userSet);
        workspace.setWorkspaceLists(listSet);

        workspaceSet.add(workspace);
        workspaceRepository.saveAll(workspaceSet);
    }

    @Test
    @DisplayName("Delete workspace by id")
    void should_Delete_By_Id() {
        // Given
        Workspace testWorkspace = workspaceRepository.findById(1L).get();
        System.out.println("Amount of workspaces in workspace repository: " + workspaceRepository.findAll().size());
        System.out.println(workspaceRepository.findById(1L).get());

        // When
        workspaceRepository.delete(testWorkspace);

        // Then
        Workspace tempWorkspace = null;
        Optional<Workspace> optionalWorkspaceList = workspaceRepository.findById(1L);

        if (optionalWorkspaceList.isPresent()) {
            tempWorkspace = optionalWorkspaceList.get();
        }
        System.out.println("Amount of workspaces in workspace repository: " + workspaceRepository.findAll().size());
        assertThat(tempWorkspace).isNull();
    }

    @Test
    void findAllByUserId() {
        // Given
        List<Workspace> expected = new ArrayList<>();
        List<User> userList = new ArrayList<>(workspaceRepository.findById(1L).get().getUsers());
        expected.add(workspaceRepository.findById(1L).get());

        // When & Then
        assertEquals(expected, workspaceRepository.findAllByUserId(userList.get(0).getUserId()));
        System.out.println("User " + userList.get(0).getName() + " has " + expected.size() + ((expected.size() > 1) ? " workspaces" : " workspace"));
        System.out.println(expected.get(0).toString());
    }
}