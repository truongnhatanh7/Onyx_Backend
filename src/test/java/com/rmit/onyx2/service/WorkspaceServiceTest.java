package com.rmit.onyx2.service;

import com.rmit.onyx2.model.*;
import com.rmit.onyx2.repository.UserRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class WorkspaceServiceTest {

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkspaceService workspaceService;

    private Workspace workspace;

    @BeforeEach
    void setUp() {
        Set<User> userSet = new HashSet<>();
        Set<WorkspaceList> listSet = new HashSet<>();
        Set<Workspace> workspaceSet = new HashSet<>();
        Set<Task> taskSet = new HashSet<>();

        workspace = new Workspace(1L, "Backend test", null, null);
        User user = new User(
                2L,
                "Tri Lai",
                "trilai",
                "123456",
                null
        );
        WorkspaceList workspaceList = new WorkspaceList(3L, "Workspace Repos test", workspace, null);

        Task task1 = new Task(
                4L,
                "Test WorkspaceListServer module",
                0,
                1,
                "Test if the WorkspaceListService can call to the repository",
                LocalDate.now(),
                workspaceList
        );
        Task task2 = new Task(
                5L,
                "Test WorkspaceListController module",
                1,
                1,
                "Test if the WorkspaceListController can call to the service",
                LocalDate.now(),
                workspaceList
        );

        user.setWorkspaces(workspaceSet);
        workspaceList.setTasks(taskSet);
        userSet.add(user);
        listSet.add(workspaceList);
        taskSet.add(task1);
        taskSet.add(task2);

        workspace.setUsers(userSet);
        workspace.setWorkspaceLists(listSet);

        workspaceSet.add(workspace);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Should get empty workspaces")
    void should_Get_Empty_Workspaces() {
        // Given
        List<Workspace> workspaceList = new ArrayList<>(workspaceRepository.findAll());
        List<WorkspaceDTO> expected = new ArrayList<>();
        given(workspaceRepository.findAll()).willReturn(workspaceList);
        workspaceList.forEach(w -> expected.add(new WorkspaceDTO(w)));

        // When
        assertEquals(expected, workspaceService.getAllWorkspaces());
    }

    @Test
    @DisplayName("Should get all the workspaces")
    void should_Get_All_Workspaces() {
        // Given
        List<Workspace> temp = new ArrayList<>();
        List<WorkspaceDTO> expected = new ArrayList<>();
        temp.add(workspace);
        given(workspaceRepository.findAll()).willReturn(temp);
        expected.add(new WorkspaceDTO(workspace));

        // When
        workspaceService.getAllWorkspaces();

        // Then
        verify(workspaceRepository).findAll();
        assertThat(expected.size()).isGreaterThan(0);
        System.out.println("est case passed: List of workspaces: ");
        expected.forEach(System.out::println);
    }

    @Test
    @DisplayName("Should add new workspace to workspace repository")
    void should_Add_Workspace() {
        WorkspaceDTO expected = new WorkspaceDTO(workspace);
        when(workspaceRepository.saveAndFlush(workspace)).thenReturn(workspace);
        assertEquals(expected, workspaceService.addWorkspace(workspace));

        System.out.println("Test case passed: The workspace \"" + workspace.getWorkspaceTitle() + "\" has been added to Workspace Repository");
    }

    @Test
    @DisplayName("Should delete the Workspace by its ID")
    void should_Delete_Workspace_By_Id() {
        workspaceRepository.save(workspace); // Add a sample workspace to the workspace repos for further being deleted

        // Add the list of tasks to the workspace
        Workspace foundWorkspace = new Workspace();
        foundWorkspace.setWorkspaceLists(workspace.getWorkspaceLists());

        // Assume the Workspace Repos return the workspace with matching id
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.of(foundWorkspace));

        // Assume the User Repos return a list of users who have a matching workspace with the needed deleting one.
        given(userRepository.findAll()).willReturn(new ArrayList<>(workspace.getUsers()));

        // Simulate the removal of workspace in user
        User testUser = workspace.getUsers().stream().findFirst().get();
        testUser.getWorkspaces().remove(workspace);

        // When
        workspaceService.deleteWorkspaceById(workspace.getWorkspaceId());

        // Then
        verify(workspaceRepository).deleteById(workspace.getWorkspaceId());
        assertEquals(testUser.getWorkspaces().size(), 0);
        assertEquals(workspaceRepository.findAll().size(), 0);
    }

    @Test
    @DisplayName("Should get list of workspaces by user id")
    void should_Get_Workspaces_By_User_Id() {
        // Given
        List<WorkspaceDTO> expected = new ArrayList<>();
        User testUser = workspace.getUsers().stream().findFirst().get();
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        testUser.getWorkspaces().forEach(w -> expected.add(new WorkspaceDTO(w)));

        // When & Then
        assertEquals(expected, workspaceService.getWorkspacesByUserId(testUser.getUserId()));
        System.out.println("Test case passed: List of workspaces by User with ID " + testUser.getUserId());
        expected.forEach(System.out::println);
    }

    @Test
    @DisplayName("Should get workspace by id")
    void should_Get_Workspace_By_Id() {
        // Given
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.of(workspace));
        WorkspaceDTO expected = new WorkspaceDTO(workspace);

        // When & Then
        assertEquals(expected, workspaceService.getWorkspaceById(workspace.getWorkspaceId()));
        System.out.println("Test case passed: " + workspace.toString());
    }

    @Test
    @DisplayName("Should get no workspace by id")
    void should_Get_No_Workspace_By_Id() {
        // Given
        given(workspaceRepository.findById(workspace.getWorkspaceId())).willReturn(Optional.empty());

        // When & Then
        // The output should be workspace={null, null, [], []}
        assertNull(workspaceService.getWorkspaceById(workspace.getWorkspaceId()).getWorkspaceId());
        assertNull(workspaceService.getWorkspaceById(workspace.getWorkspaceId()).getWorkspaceTitle());
        assertEquals(new ArrayList(), workspaceService.getWorkspaceById(workspace.getWorkspaceId()).getWorkspaceLists());
        assertEquals(new ArrayList(), workspaceService.getWorkspaceById(workspace.getWorkspaceId()).getUsers());
        System.out.println("Test case passed: There is no workspace with the id " + workspace.getWorkspaceId());
    }
}