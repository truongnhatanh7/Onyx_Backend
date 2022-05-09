package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private List<Workspace> workspaceSet = new ArrayList<>();
    private Set<WorkspaceList> workspaceLists = new HashSet<>();
    private Set<User> userSet = new HashSet<>();
    private Set<Task> taskSet = new HashSet<Task>();

    // Prepare the data including User, Workspace, WorkspaceList, Task
    @BeforeEach
    void setUp() throws Exception{
        testDataGenerator();
    }

    // Clear everything after the test
    @AfterEach
    void tearDown() {
        taskRepository.deleteAll();
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

        taskRepository.saveAll(taskSet);
    }

    @Test
    @Rollback(value = false)
    @Order(3)
    void should_Delete_By_Exist_Id() throws Exception{
        // Given
        Task task = new Task(); // Target task
        Long taskId = 3L;

        Task taskDeleted = null; // Used for make assertions

        if (taskRepository.findById(taskId).isPresent()) {
            task = taskRepository.findById(taskId).get();
        }

        // When
        taskRepository.delete(task);

        // Then
        Optional<Task> optionalTask = taskRepository.findById(taskId);

        // Try to get the deleted task, if it is not present meaning that the target task
        // has been successfully deleted; Otherwise, the test will fail.
        if (optionalTask.isPresent()) {
            taskDeleted = optionalTask.get();
        }

        assertThat(taskDeleted).isNull();
    }

    @Test
    @Rollback(value = false)
    @Order(4)
    void should_Not_Delete_By_Non_Exist_Id() {
        // Given
        Task task = new Task(); // Target task
        Long taskId = 10L;

        Task taskDeleted = null; // Used for make assertions

        if (taskRepository.findById(taskId).isPresent()) {
            task = taskRepository.findById(taskId).get();
        }

        // When
        taskRepository.delete(task);

        // Then
        assertThat(task.getTaskId()).isNull();
    }


    @Test
    @Rollback(value = false)
    @Order(1)
    void should_Update_Pos() {
        // Given
        Task task = new Task();
        Long taskId = 3L;
        Integer newPos = 5;

        if (taskRepository.findById(taskId).isPresent()) {
            task = taskRepository.findById(taskId).get();
        }
        System.out.println(task);

        // When
        task.setPos(newPos);

        // Then
        assertThat(task.getPos()).isEqualTo(newPos);
        System.out.println(task);
    }

    @Test
    @Order(2)
    void should_Update_Priority() {
        // Given
        Task task = new Task();
        Long taskId = 3L;
        Integer newPriority = 3;

        if (taskRepository.findById(taskId).isPresent()) {
            task = taskRepository.findById(taskId).get();
        }

        // When
        task.setPriority(newPriority);

        // Then
        assertThat(task.getPriority()).isEqualTo(newPriority);
        System.out.println(task);
    }
}