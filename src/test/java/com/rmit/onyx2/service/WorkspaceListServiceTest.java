package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.junit.Rule;
import org.junit.jupiter.api.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WorkspaceListServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private WorkspaceListRepository workspaceListRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private WorkspaceListService workspaceListService;

    private final Set<Task> taskSet = new HashSet<>();
    private final Set<WorkspaceList> listSet = new HashSet<>();
    private final List<Workspace> workspaceSet = new ArrayList<>();
    private final Set<User> userSet = new HashSet<>();
    private WorkspaceList workspaceList;
    private Workspace workspace;


    @Before
    public void setUp() {
        workspace = new Workspace(1L, "Backend test", null, null, null);
        workspaceList = new WorkspaceList(2L, "Unit testing", workspace, null);
        Task task = new Task(
                3L,
                "Test WorkspaceListServer module",
                0,
                1,
                "Test if the WorkspaceListService can call to the repository",
                LocalDate.now(),
                workspaceList);
        User testUser = new User(
                4L,
                "Tri Lai",
                "trilai",
                "123456",
                null,
                null
        );

        workspace.setOwnerId(testUser.getUserId());
        taskSet.add(task);
        listSet.add(workspaceList);
        workspaceSet.add(workspace);
        userSet.add(testUser);

        workspaceList.setTasks(taskSet);
        workspace.setWorkspaceLists(listSet);
        testUser.setWorkspaces(workspaceSet);
        workspace.setUsers(userSet);
    }

    @After
    public void tearDown() {
        workspaceRepository.deleteAll();
        workspaceListRepository.deleteAll();
        taskRepository.deleteAll();
        workspace = null;
        workspaceList = null;
        userSet.clear();
        listSet.clear();
        workspaceSet.clear();
        taskSet.clear();
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    @DisplayName("Should return list of WorkspaceList with exist workspaceId provided")
   public void check_Return_List_When_Workspace_Present() {
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.of(workspace));

        assertEquals(workspace.getWorkspaceLists().size(),
                workspaceListService.getWorkspaceListByWorkspaceId(workspace.getWorkspaceId()).size());

        System.out.println("Test case passed: List of Workspace Lists: ");
        workspace.getWorkspaceLists().forEach(workspaceList -> System.out.println(workspaceList.toString()));
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    @DisplayName("Should return null with non-exist workspaceId provided")
    public void check_Return_List_When_Workspace_Is_Not_Present() {
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.empty());

        assertNull(workspaceListService.getWorkspaceListByWorkspaceId(workspace.getWorkspaceId()));
        System.out.println("Test case passed: No WorkspaceList found in workspace " + workspace.getWorkspaceTitle());
    }

    @Test
    @Order(3)
    @Rollback(value = false)
    @DisplayName("Should add WorkspaceList successfully")
    public void test_Add_List_By_WorkspaceId_When_ListRepo_Size_Greater_Than_Zero() {
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.of(workspace));
        given(workspaceListRepository.findAll()).willReturn(new ArrayList<>(listSet));
        workspaceListService.addWorkspaceListByWorkspaceId(workspace.getWorkspaceId(), workspaceList);
        verify(workspaceListRepository).save(workspaceList);
        System.out.println("Test case passed: The WorkspaceList \"" + workspaceList.getName() + "\" has been added to " + workspace.getWorkspaceTitle());
    }

    @Test
    @Order(2)
    @DisplayName("Should return null WorkspaceListDTO instance when WorkspaceListRepo does not contains any WorkspaceList")
    public void test_Add_List_By_WorkspaceId_When_ListRepo_Size_Equal_Zero() {
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.of(workspace));
        given(workspaceListRepository.findAll()).willReturn(new ArrayList<>());
        workspaceListService.addWorkspaceListByWorkspaceId(workspace.getWorkspaceId(), workspaceList);
        verify(workspaceListRepository).save(workspaceList);
        System.out.println("Test case passed: The WorkspaceListRepository contains no WorkspaceList objects!");
    }

    @Test
    @Order(1)
    @DisplayName("Should return null WorkspaceListDTO when workspace is not exist")
    public void test_Add_List_By_WorkspaceId_Which_Not_Present() {
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.empty());
        workspaceListService.addWorkspaceListByWorkspaceId(workspace.getWorkspaceId(), workspaceList);
        System.out.println("Test case passed: The provided workspaceId " + workspace.getWorkspaceId() + " does not exist!");
    }

    @Test
    @Order(7)
    @Rollback(value = false)
    @DisplayName("Should delete WorkspaceList with given its id")
    public void should_Delete_WorkspaceList_By_Id() {
        given(workspaceListRepository.findById(workspaceList.getListId())).willReturn(Optional.of(workspaceList));
        workspaceListService.deleteWorkspaceListById(workspaceList.getListId());
        verify(workspaceListRepository).deleteById(workspaceList.getListId());
        System.out.println("Test case passed: The WorkspaceList \"" + workspaceList.getName() + "\" has been removed from " + workspace.getWorkspaceTitle());
    }
}