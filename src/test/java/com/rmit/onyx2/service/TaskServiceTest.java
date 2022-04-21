package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assumptions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class TaskServiceTest {

    @Mock
    private WorkspaceListRepository workspaceListRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Set<Workspace> workspaceSet = new HashSet<>();
    private Set<WorkspaceList> workspaceLists = new HashSet<>();
    private Set<User> userSet = new HashSet<>();
    private Set<Task> taskSet = new HashSet<Task>();

    @BeforeEach
    void setUp() {
        // Create mock object for fields annotated with @Mock
        // Create instance of fields annotated with @InjectMock and inject the mock object into it
        taskService = new TaskService(taskRepository, workspaceListRepository);
//        testDataGenerator();
    }

//    void testDataGenerator() {
//        User user = new User(
//                1L,
//                "Tri Lai",
//                "trilai",
//                "123456",
//                workspaceSet
//        );
//        userSet.add(user);
//
//        Workspace workspace = new Workspace(
//                1L,
//                "Unit Testing",
//                userSet,
//                workspaceLists
//        );
//        workspaceSet.add(workspace);
//
//        WorkspaceList workspaceList = new WorkspaceList(
//                1L,
//                "Task Service module",
//                workspace,
//                taskSet
//        );
//        workspaceLists.add(workspaceList);
//
//        Task task1 = new Task(
//                0L,
//                "Test deleteTaskById method",
//                0,
//                0,
//                "Check if the system can delete the task when provide its id",
//                workspaceList
//        );
//
//        Task task2 = new Task(
//                1L,
//                "Test getAllTasksByListId method",
//                1,
//                0,
//                "Check if the system can get all the tasks when provide list id",
//                workspaceList
//        );
//        taskSet.add(task1);
//        taskSet.add(task2);
//
//        taskRepository.save(task1);
//        taskRepository.save(task2);
//        workspaceListRepository.saveAll(workspaceLists);
//    }

    @Test
    void should_Get_All_Tasks_By_ListId() {
//        // Way 1: Still implement
//        // Given
//        Long listId = 3L;
//        WorkspaceList workspaceList = new WorkspaceList();
//
//        // When
//        System.out.println(workspaceListRepository.findAll().size());
//
//        if (workspaceListRepository.findById(listId).isPresent())
//            workspaceList = workspaceListRepository.findById(listId).get();
//
//        System.out.println(workspaceList);
//        // Then
//        assertThat(workspaceList.getTasks().size()).isGreaterThan(0);

        // Way 2: Verify
        // Given
        Long listId = 3L;

        // When
        taskService.getAllTasksByListId(listId);

        // Then
        verify(workspaceListRepository).findById(listId);
    }

    @Test
    @Disabled("Not test yet")
    void should_Edit_Task() {
    }

    //
    @Test
    void should_Add_Task_By_ListId_whose_list_size_greater_than_zero() {
        // Given
        Long listId = 3L;

        Task newTask = new Task(
                3L,
                "Edit weejo",
                3,
                0,
                "Edit my recorded weejo and later render it",
                LocalDate.now(),
                null
        );

        Set<Task> tasks = new HashSet<>();
        tasks.add(newTask);

        WorkspaceList foundList = new WorkspaceList(
                1L,
                "Testing TaskRepository",
                null,
                tasks
        );

        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);

        // Assume the workspace list is found
        given(workspaceListRepository.findById(listId)).willReturn(Optional.of(foundList));

        // Assume the list of task have more than 1 task
        List<Task> taskListSize = new ArrayList<Task>(1);
        taskListSize.add(newTask);
        given(taskRepository.findAll()).willReturn(taskListSize);

        // When
        taskService.addTaskByListId(listId, newTask);
        System.out.println(taskListSize.size());

        // Then
        verify(taskRepository).save(taskArgumentCaptor.capture());
        assertThat(taskArgumentCaptor.getValue()).isEqualTo(newTask);
    }

    @Test
    void should_Add_Task_By_ListId_whose_list_size_equal_zero() {
        // Given
        Long listId = 3L;
        Task newTask = new Task(
                3L,
                "Edit weejo",
                3,
                0,
                "Edit my recorded weejo and later render it",
                LocalDate.now(),
                null
        );
        Set<Task> tasks = new HashSet<>();
        tasks.add(newTask);

        WorkspaceList foundList = new WorkspaceList(
                1L,
                "Testing TaskRepository",
                null,
                tasks
        );

        ArgumentCaptor<Task> taskArgumentCaptor = ArgumentCaptor.forClass(Task.class);

        // Assume the workspace list is found
        given(workspaceListRepository.findById(listId)).willReturn(Optional.of(foundList));

        // When
        taskService.addTaskByListId(listId, newTask);

        // Then
        verify(taskRepository).save(taskArgumentCaptor.capture());
        assertThat(taskArgumentCaptor.getValue()).isEqualTo(newTask);
    }

    @Test
    @Disabled("Not test yet")
    void reject_Add_Task_By_ListId_when_list_not_found() {
        // Given


        // When

        // Then
    }

    @Test
    void should_delete_Task_By_Id() {
        // Given
        Long taskId = 3L;
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);

        // When
        taskService.deleteTaskById(taskId);

        // Then
        verify(taskRepository).deleteById(taskIdCaptor.capture());
        assertThat(taskIdCaptor.getValue()).isEqualTo(taskId);
    }

    @Test
    void should_Set_Pos() {
        // Given
        Long taskId = 3L;
        Task foundTask = new Task(
                3L,
                "Edit weejo",
                3,
                0,
                "Edit my recorded weejo and later render it",
                LocalDate.now(),
                null
        );

        given(taskRepository.findById(taskId)).willReturn(java.util.Optional.of(foundTask));

        // When
        taskService.setPos(taskId, 5);

        // Then
        verify(taskRepository).updatePos(taskId, 5);
    }

    @Test
    void should_Set_Priority() {
        // Given
        Long taskId = 3L;
        Task foundTask = new Task(
                3L,
                "Edit weejo",
                3,
                0,
                "Edit my recorded weejo and later render it",
                LocalDate.now(),
                null
        );

        given(taskRepository.findById(taskId)).willReturn(java.util.Optional.of(foundTask));

        // When
        taskService.setPriority(taskId, 3);

        // Then
        verify(taskRepository).updatePriority(taskId, 3);
    }

    @Test
    @Disabled("Not done yet")
    void should_Set_Desc() {
        // Given
        Long taskId = 3L;
        Task foundTask = new Task(
                3L,
                "Edit weejo",
                3,
                0,
                "Edit my recorded weejo and later render it",
                LocalDate.now(),
                null
        );

        given(taskRepository.findById(taskId)).willReturn(java.util.Optional.of(foundTask));

        // When
        taskService.setDesc(taskId, "Update the weejo to my Youtube channel");

        // Then
    }
}