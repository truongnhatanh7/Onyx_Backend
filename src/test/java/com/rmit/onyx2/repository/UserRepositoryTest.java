package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private final List<Workspace> workspaceSet = new ArrayList<>();
    private final Set<WorkspaceList> workspaceLists = new HashSet<>();
    private final Set<User> userSet = new HashSet<>();
    private final Set<Task> taskSet = new HashSet<>();

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
                null,
                workspaceSet
        );
        userSet.add(user);
        userRepository.saveAll(userSet);

        Workspace workspace = new Workspace(
                1L,
                "Unit Testing",
                user.getUserId(),
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
                LocalDate.now(),
                workspaceList
        );

        Task task2 = new Task(
                0L,
                "Test foo method",
                1,
                0,
                "Check foo",
                LocalDate.now(),
                workspaceList
        );
        taskSet.add(task1);
        taskSet.add(task2);

    }

    @Test
    @DisplayName("Delete User by their ID")
    void deleteById() {
        // Given
        User user = new User(); // Target task
        Long userId = 1L;

        User userDeleted = null; // Used for make assertions

        if (userRepository.findById(userId).isPresent()) {
            user = userRepository.findById(userId).get();
        }

        System.out.println("-------------User Repository size-------------");
        System.out.println("Before: " + userRepository.findAll().size());

        // When
        userRepository.delete(user);
        System.out.println("After: " + userRepository.findAll().size());

        // Then
        Optional<User> optionalUser = userRepository.findById(userId);

        // Try to get the deleted task, if it is not present meaning that the target task
        // has been successfully deleted; Otherwise, the test will fail.
        if (optionalUser.isPresent()) {
            userDeleted = optionalUser.get();
        }

        assertThat(userDeleted).isNull();
        System.out.println("Test case passed: Delete User with given ID successfully");
    }
}