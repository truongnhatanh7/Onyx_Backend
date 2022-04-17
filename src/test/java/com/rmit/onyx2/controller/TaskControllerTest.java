package com.rmit.onyx2.controller;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class TaskControllerTest {
    @Mock
    TaskService taskService;

    @InjectMocks
    TaskController taskController;

    @Test
    void should_Get_All_Tasks_By_ListId() {
        // Given
        Long listId = 1L;
        ArgumentCaptor<Long> listIdCaptor = ArgumentCaptor.forClass(Long.class);

        // When
        taskController.getAllTasksByListId(listId);

        // Then
        verify(taskService).getAllTasksByListId(listIdCaptor.capture());
        assertThat(listIdCaptor.getValue()).isEqualTo(listId);
    }

    @Test
    void should_Add_Task_By_ListId() {
        // Given
        Long listId = 1L;
        Task newTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
                null
        );

        ArgumentCaptor<Long> listIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        // When
        taskController.addTaskByListId(listId, newTask);

        // Then
        verify(taskService).addTaskByListId(listIdCaptor.capture(), taskCaptor.capture());
        assertThat(listIdCaptor.getValue()).isEqualTo(listId);
        assertThat(taskCaptor.getValue()).isEqualTo(newTask);
    }

    @Test
    void should_edit_task_without_destination_listId() {
        // Given
        Task editTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
                null
        );

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);

        // When
        taskController.editTask(editTask);

        // Then
        verify(taskService).editTask(taskCaptor.capture());
        assertThat(taskCaptor.getValue()).isEqualTo(editTask);
    }

    @Test
    void should_edit_task_with_destination_listId() {
        // Given
        Task editTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
                null
        );

        Long destinationListId = 1L;

        ArgumentCaptor<Task> taskCaptor = ArgumentCaptor.forClass(Task.class);
        ArgumentCaptor<Long> destinationListIdCaptor = ArgumentCaptor.forClass(Long.class);

        // When
        taskController.editTask(editTask, destinationListId);

        // Then
        verify(taskService).editTask(taskCaptor.capture(), destinationListIdCaptor.capture());
        assertThat(taskCaptor.getValue()).isEqualTo(editTask);
        assertThat(destinationListIdCaptor.getValue()).isEqualTo(destinationListId);
    }

    @Test
    void should_Delete_Task_By_Id() {
        // Given
        Task deleteTask = new Task(
                3L,
                "Edit video",
                3,
                0,
                "Edit my recorded video and later render it",
                null
        );
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);

        // When
        taskController.deleteTaskById(deleteTask.getTaskId());

        // Then
        verify(taskService).deleteTaskById(taskIdCaptor.capture());
        assertThat(taskIdCaptor.getValue()).isEqualTo(deleteTask.getTaskId());
    }

    @Test
    void should_Set_Pos() {
        // Given
        Long taskId = 3L;
        Integer newPos = 1;
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> newPosCaptor = ArgumentCaptor.forClass(Integer.class);

        // When
        taskController.setPos(taskId, newPos);

        // Then
        verify(taskService).setPos(taskIdCaptor.capture(), newPosCaptor.capture());
        assertThat(taskIdCaptor.getValue()).isEqualTo(taskId);
        assertThat(newPosCaptor.getValue()).isEqualTo(newPos);
    }

    @Test
    void should_Set_Desc() {
        // Given
        Long taskId = 3L;
        String newDesc = "Upload the video to my Youtube channel";
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<String> newDescCaptor = ArgumentCaptor.forClass(String.class);

        // When
        taskController.setDesc(taskId, newDesc);

        // Then
        verify(taskService).setDesc(taskIdCaptor.capture(), newDescCaptor.capture());
        assertThat(taskIdCaptor.getValue()).isEqualTo(taskId);
        assertThat(newDescCaptor.getValue()).isEqualTo(newDesc);
    }

    @Test
    void should_Set_Priority() {
        // Given
        Long taskId = 3L;
        Integer newPriority = 2;
        ArgumentCaptor<Long> taskIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Integer> newPriorityCaptor = ArgumentCaptor.forClass(Integer.class);

        // When
        taskController.setPriority(taskId, newPriority);

        // Then
        verify(taskService).setPriority(taskIdCaptor.capture(), newPriorityCaptor.capture());
        assertThat(taskIdCaptor.getValue()).isEqualTo(taskId);
        assertThat(newPriorityCaptor.getValue()).isEqualTo(newPriority);
    }
}