package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.WorkspaceList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WorkspaceListRepositoryTest {

    @Autowired
    private WorkspaceListRepository workspaceListRepository;

    @BeforeEach
    void setUp() {
        WorkspaceList workspaceList = new WorkspaceList(1L, "Unit testing", null, null);
        workspaceListRepository.save(workspaceList);
    }

    @Test
    @DisplayName("Delete list by id")
    void should_Delete_By_Id() {
        // Given
        WorkspaceList testWorkspaceList = workspaceListRepository.findById(1L).get();
        System.out.println("Amount of lists within the workspace: " + workspaceListRepository.findAll().size());
        // When
        workspaceListRepository.delete(testWorkspaceList);

        // Then
        WorkspaceList tempWorkspaceList = null;
        Optional<WorkspaceList> optionalWorkspaceList = workspaceListRepository.findById(1L);

        if (optionalWorkspaceList.isPresent()) {
            tempWorkspaceList = optionalWorkspaceList.get();
        }
        System.out.println("Amount of lists within the workspace: " + workspaceListRepository.findAll().size());
        assertThat(tempWorkspaceList).isNull();
    }
}