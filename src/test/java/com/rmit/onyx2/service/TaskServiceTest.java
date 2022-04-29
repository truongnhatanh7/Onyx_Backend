package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class TaskServiceTest {

    @Mock
    private WorkspaceListRepository workspaceListRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        // Create mock object for fields annotated with @Mock
        // Create instance of fields annotated with @InjectMock and inject the mock object into it
        taskService = new TaskService(taskRepository, workspaceListRepository);
    }

    @Test
    @DisplayName("Should returns tasks with given listID")
    void should_Get_All_Tasks_By_ListId() {
        // Given
        Long listId = 3L;

        // When
        taskService.getAllTasksByListId(listId);

        // Then
        verify(workspaceListRepository).findById(listId);
    }

    @Test
    @DisplayName("Return the newly added task when Task Repo contains at least 1 task")
    void should_Add_Task_By_ListId_whose_list_size_greater_than_zero() {
        // Given
        Long listId = 3L;

        Task newTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
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
        List<Task> taskListSize = new ArrayList<>(1);
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
    @DisplayName("Return the empty task when Task Repo contains no tasks")
    void should_Add_Task_By_ListId_whose_list_size_equal_zero() {
        // Given
        Long listId = 3L;
        Task newTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
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
    @DisplayName("Return empty Task when no lists found in the workspaceList repository")
    void reject_Add_Task_By_ListId_when_list_not_found() {
        // Given
        Long listId = 3L;
        Task newTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
                LocalDate.now(),
                null
        );
           
        // Assume workspaceListRepository does not find any matching workspaceList objects
        given(workspaceListRepository.findById(listId)).willReturn(Optional.empty());

        // When
        taskService.addTaskByListId(listId, newTask);

        // Then
        verify(workspaceListRepository).findById(listId);
    }

    @Test
    @DisplayName("Should delete task with given taskID")
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
    @DisplayName("Should update the task's position")
    void should_Set_Pos() {
        // Given
        Long taskId = 3L;
        Task foundTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
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
    @DisplayName("Should update the task's priority")
    void should_Set_Priority() {
        // Given
        Long taskId = 3L;
        Task foundTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
                LocalDate.now(),
                null
        );

        given(taskRepository.findById(taskId)).willReturn(java.util.Optional.of(foundTask));

        // When
        taskService.setPriority(taskId, 3);

        // Then
        verify(taskRepository).updatePriority(taskId, 3);
    }
}