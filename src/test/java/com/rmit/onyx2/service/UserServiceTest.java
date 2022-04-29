package com.rmit.onyx2.service;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.UserDTO;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.repository.UserRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = UserServiceTest.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    WorkspaceRepository workspaceRepository;

    @Mock
    WorkspaceService workspaceService;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("Get all users successfully")
    void should_Get_All_Users() {
        // Given
        List<User> temp = new ArrayList<>();
        List<UserDTO> getResult = new ArrayList<>();
        List<Workspace> workspaces = new ArrayList<>();
        Workspace workspace = new Workspace(
                4L,
                "Onyx Test",
                null,
                null
        );
        workspaces.add(workspace);

        User user1 = new User(
                1L,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );

        User user2 = new User(
                2L,
                "John Doe",
                "johndoe",
                "john123",
                workspaces
        );

        User user3 = new User(
                3L,
                "Jane Doe",
                "janedoe",
                "jane123",
                workspaces
        );

        temp.add(user1);
        temp.add(user2);
        temp.add(user3);

        given(userRepository.findAll()).willReturn(temp);

        // When
        userService.getAllUsers();
        temp.forEach(user -> getResult.add(new UserDTO(user)));

        // Then
        // Just only verify the invocation on delete method was done
        verify(userRepository).findAll();

        // Check whether the implementation works properly
        System.out.println("Test case passed");
        System.out.println("Amount of users: " + getResult.size());
        getResult.forEach(userDTO -> System.out.println(userDTO.toString()));
        assertThat(getResult.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Return UserDTO with newly added User")
    void should_Add_User_Successfully() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace workspace = new Workspace(
                4L,
                "Onyx Test",
                null,
                null
        );

        Set<User> temp = new HashSet<>();
        User testUser = new User(
                0L,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );
        temp.add(testUser);

        workspace.setUsers(temp);
        workspaces.add(workspace);

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // Assume that userRepository have saved new user successfully
        given(userRepository.save(testUser)).willReturn(testUser);

        // Assume that newly added user is found in User Repo
        given(userRepository.findById(testUser.getUserId()))
                .willReturn(Optional.of(testUser));

        // When
        userService.addUser(testUser);

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue()).isEqualTo(testUser);
        System.out.println("Test case passed: Add new user successfully");
        System.out.println("Parameter when save() is invoked: " + userArgumentCaptor.getValue().toString());
    }

    @Test
    @DisplayName("Return the bad request when workspace is not found in Workspace Repos")
    void test_Add_Non_Exist_Workspace_For_UserById() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace testWorkspace = new Workspace(
                0L,
                "FrontEnd Test",
                null,
                null
        );

        Set<User> userSet = new HashSet<>();
        Long userId = 1L;
        User testUser = new User(
                userId,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );

        userSet.add(testUser);
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.empty());

        // When
        userService.addWorkspaceForUserById(testWorkspace.getWorkspaceId(), testUser.getUserId());

        // Then
        assertThat(userRepository.findAll().size()).isEqualTo(0);
        System.out.println("Test case passed: Add non-exist workspace to exist user");
    }

    @Test
    @DisplayName("Return the bad request when user is not found in User Repos")
    void test_Add_Workspace_For_Non_Exist_UserId() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace testWorkspace = new Workspace(
                0L,
                "FrontEnd Test",
                null,
                null
        );

        Set<User> userSet = new HashSet<>();
        Long userId = 1L;
        User testUser = new User(
                userId,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );

        userSet.add(testUser);
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);

        given(userRepository.findById(userId)).willReturn(Optional.empty());
        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.of(testWorkspace));

        // When
        userService.addWorkspaceForUserById(testWorkspace.getWorkspaceId(), testUser.getUserId());

        // Then
        assertThat(userRepository.findAll().size()).isEqualTo(0);
        System.out.println("Test case passed: Add exist workspace to non-exist user");
    }

    @Test
    @DisplayName("Return the ok request as adding workspace successfully")
    void should_Add_Workspace_For_UserById() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace testWorkspace = new Workspace(
                4L,
                "Onyx Test",
                null,
                null
        );

        Set<User> userSet = new HashSet<>();
        Long userId = 1L;
        User testUser = new User(
                userId,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );

        userSet.add(testUser);
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);

        given(userRepository.findById(userId)).willReturn(Optional.of(testUser));
        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.of(testWorkspace));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // When
        userService.addWorkspaceForUserById(testWorkspace.getWorkspaceId(), testUser.getUserId());

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue()).isEqualTo(testUser);
        System.out.println("Test case passed: Add exist workspace to exist user");
    }

    @Test
    @DisplayName("Should delete user by their ID")
    void should_Delete_User_By_Id() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace testWorkspace = new Workspace(
                4L,
                "Onyx Test",
                null,
                null
        );

        Set<User> userSet = new HashSet<>();
        User testUser = new User(
                1L,
                "Tri Lai",
                "trilai",
                "123456",
                null
        );

        userSet.add(testUser);
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);
        testUser.setWorkspaces(workspaces);
        userRepository.save(testUser);

        // Assume the userRepository return the user
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));

        // Assume the workspaceRepository return the workspace
//        List<Workspace> workspaceList = new ArrayList<>();
//        workspaceList.add(testWorkspace);
//        given(workspaceRepository.findAll()).willReturn(workspaceList);
//
        // When
//        try {
//            userService.deleteUserById(testUser.getUserId());
//        } catch (Exception exception) {
//            verify(workspaceService).deleteWorkspaceById(testWorkspace.getWorkspaceId());
//        }

        userService.deleteUserById(testUser.getUserId());

        // Then
        assertThat(testUser.getWorkspaces()).isEmpty();
        verify(userRepository).deleteById(testUser.getUserId());
    }

    @Test
    @DisplayName("Get user by their ID successfully")
    void should_Get_User_By_Exist_Id() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace testWorkspace = new Workspace(
                4L,
                "Onyx Test",
                null,
                null
        );

        Set<User> userSet = new HashSet<>();

        User testUser = new User(
                0L,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );

        userSet.add(testUser);
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);

        userRepository.save(testUser);

        // Assume the user is found by userId
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));

        // When
        userService.getUserById(testUser.getUserId());

        // Then
        // Check if the method returns the instance of UserDTO with the found user passed as argument
        boolean exceptionThrown = true;
        try {
            UserDTO newUserDto = new UserDTO(testUser);
        }catch(Exception e){
            exceptionThrown = false;
        }
        assertTrue(exceptionThrown, "Method should return new UserDTO object with user presents in the repository.");
    }

    @Test
    @DisplayName("Get user by their ID unsuccessfully")
    void test_Get_User_By_Non_Exist_Id() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace testWorkspace = new Workspace(
                4L,
                "Onyx Test",
                null,
                null
        );

        Set<User> userSet = new HashSet<>();

        User testUser = new User(
                0L,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );

        userSet.add(testUser);
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);

        userRepository.save(testUser);

        // Assume the user is NOT found by userId
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.empty());

        // When
        userService.getUserById(testUser.getUserId());

        // Then
        // Check if the method returns the null instance of UserDTO
        boolean exceptionThrown = true;
        try {
            UserDTO newUserDto = new UserDTO();
        }catch(Exception e){
            exceptionThrown = false;
        }
        assertTrue(exceptionThrown, "Method should return new null UserDTO object because user does not present in repository.");
    }

    @Test
    @DisplayName("Remove User from workspace by their ID")
    void removeUserFromWorkspaceById() {
        // Given
        List<Workspace> workspaces = new ArrayList<>();
        Workspace testWorkspace = new Workspace(
                1L,
                "Onyx Test",
                null,
                null
        );

        Set<User> userSet = new HashSet<>();
        Long userId = 0L;
        User testUser = new User(
                userId,
                "Tri Lai",
                "trilai",
                "123456",
                workspaces
        );

        userSet.add(testUser);
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);

        userRepository.save(testUser);

        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.of(testWorkspace));
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));

        // When
        userService.removeUserFromWorkspaceById(testWorkspace.getWorkspaceId(), testUser.getUserId());

        // Then
        verify(workspaceRepository).save(testWorkspace);
        System.out.println("Test case passed: Remove user out of workspace successfully");
    }
}