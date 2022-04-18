package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private Set<Workspace> workspaceSet = new HashSet<>();
    private Set<WorkspaceList> workspaceLists = new HashSet<>();
    private Set<User> userSet = new HashSet<>();
    private Set<Task> taskSet = new HashSet<>();

    // Prepare the data including User, Workspace, WorkspaceList, Task
    @BeforeEach
    void setUp() {
        testDataGenerator();
    }

    // Clear everything after the test
    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    void testDataGenerator() {
        User user = new User(
                1L,
                "Tri Lai",
                "trilai",
                "123456",
                workspaceSet
        );
        userSet.add(user);
        userRepository.saveAll(userSet);

        Workspace workspace = new Workspace(
                1L,
                "Unit Testing",
                userSet,
                workspaceLists
        );
        workspaceSet.add(workspace);

        WorkspaceList workspaceList = new WorkspaceList(
                1L,
                "Task Service module",
                workspace,
                taskSet
        );
        workspaceLists.add(workspaceList);

        Task task1 = new Task(
                0L,
                "Test deleteTaskById method",
                0,
                0,
                "Check if the system can delete the task when provide its id",
                workspaceList
        );

        Task task2 = new Task(
                0L,
                "Test foo method",
                1,
                0,
                "Check foo",
                workspaceList
        );
        taskSet.add(task1);
        taskSet.add(task2);

    }

    @Test
    void deleteById() {
        // Given
        User user = new User(); // Target task
        Long userId = 1L;

        User userDeleted = null; // Used for make assertions

        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        }

        // When
        userRepository.delete(user);

        // Then
        Optional<User> optionalUser = userRepository.findById(userId);

        // Try to get the deleted task, if it is not present meaning that the target task
        // has been successfully deleted; Otherwise, the test will fail.
        if (optionalUser.isPresent()) {
            userDeleted = optionalUser.get();
        }

        assertThat(userDeleted).isNull();
    }
}