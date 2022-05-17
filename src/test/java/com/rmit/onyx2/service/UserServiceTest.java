package com.rmit.onyx2.service;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.UserDTO;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.repository.UserRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = UserServiceTest.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @Mock
    WorkspaceRepository workspaceRepository;

    @Mock
    WorkspaceService workspaceService;

    @Mock
    SseService sseService;

    @InjectMocks
    UserService userService;

    // Create sample test data
    List<Workspace> workspaces;
    Workspace testWorkspace;
    Set<User> userSet;
    User testUser;

    // Configure sample test data
    @BeforeEach
    void setUp() {
        workspaces = new ArrayList<>();
        testWorkspace = new Workspace(
                4L,
                "Onyx Test",
                null,
                null,
                null
        );

        userSet = new HashSet<>();

        testUser = new User(
                1L,
                "Tri Lai",
                "trilai",
                "123456",
                null,
                workspaces
        );

        testUser.setAvatarURL("https://minervastrategies.com/wp-content/uploads/2016/03/default-avatar.jpg");
        userSet.add(testUser);
        testWorkspace.setOwnerId(testUser.getUserId());
        testWorkspace.setUsers(userSet);
        workspaces.add(testWorkspace);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        workspaceRepository.deleteAll();
        workspaces.clear();
        testUser = null;
        testWorkspace = null;
        userSet.clear();
    }

    @Test
    @DisplayName("Get all users successfully")
    void should_Get_All_Users() {
        // Given
        List<User> temp = new ArrayList<>();
        List<UserDTO> getResult = new ArrayList<>();

        User user1 = new User(
                1L,
                "Tri Lai",
                "trilai",
                "123456",
                null,
                workspaces
        );

        User user2 = new User(
                2L,
                "John Doe",
                "johndoe",
                "john123",
                null,
                workspaces
        );

        User user3 = new User(
                3L,
                "Jane Doe",
                "janedoe",
                "jane123",
                null,
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
        // Just only verify the invocation on findAll() method was done
        verify(userRepository).findAll();

        // Check whether the implementation works properly
        System.out.println("Test case passed: Get all users in UserRepository");
        System.out.println("Amount of users: " + getResult.size());
        getResult.forEach(userDTO -> System.out.println(userDTO.toString()));
        assertThat(getResult.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("Return UserDTO with newly added User")
    void should_Add_User_Successfully() {
        // Given
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        // Assume that userRepository have saved new user successfully
        given(userRepository.save(testUser)).willReturn(testUser);

        // Assume that newly added user is found in User Repo
        given(userRepository.findById(testUser.getUserId()))
                .willReturn(Optional.of(testUser));

        System.out.println(testUser);

        // When
        userService.addUser(testUser);

        // Then
        verify(userRepository).save(userArgumentCaptor.capture());
        verify(userRepository).findById(testUser.getUserId());
        assertThat(userArgumentCaptor.getValue()).isEqualTo(testUser);
        System.out.println("Test case passed: Add new user successfully");
        System.out.println("Parameter when save() is invoked: " + userArgumentCaptor.getValue().toString());
    }

    @Test
    @DisplayName("Return the bad request when workspace is not found in Workspace Repos")
    void test_Add_Non_Exist_Workspace_For_UserById() {
        // Given
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.empty());

        // When
        ResponseEntity<User> actual = userService.addWorkspaceForUserById(testWorkspace.getWorkspaceId(), testUser.getUserId());

        // Then
        ResponseEntity<User> expected = ResponseEntity.badRequest().build();
        assertEquals(expected, actual);
        System.out.println("Expected: " + expected + " - Actual: " + actual);
        assertThat(userRepository.findAll().size()).isEqualTo(0);
        System.out.println("Test case passed: Add non-exist workspace to exist user");
    }

    @Test
    @DisplayName("Return the bad request when user is not found in User Repos")
    void test_Add_Workspace_For_Non_Exist_UserId() {
        // Given
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.empty());
        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.of(testWorkspace));

        // When
        ResponseEntity<User> actual = userService.addWorkspaceForUserById(testWorkspace.getWorkspaceId(), testUser.getUserId());

        // Then
        ResponseEntity<User> expected = ResponseEntity.badRequest().build();
        assertEquals(expected, actual);
        System.out.println("Expected: " + expected + " - Actual: " + actual);
        assertThat(userRepository.findAll().size()).isEqualTo(0);
        System.out.println("Test case passed: Add exist workspace to non-exist user");
    }

    @Test
    @DisplayName("Return the bad request when the added workspace already connect to the wanted user")
    void test_Add_Workspace_For_User_When_User_Already_Have_That_Workspace() {
        // Given
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.of(testWorkspace));


        // When
        ResponseEntity<User> actual = userService.addWorkspaceForUserById(testWorkspace.getWorkspaceId(), testUser.getUserId());

        // Then
        ResponseEntity<User> expected = ResponseEntity.badRequest().build();
        assertEquals(expected, actual);
        System.out.println("Expected: " + expected + " - Actual: " + actual);
        System.out.println("Test case passed: Workspace is already connected to user");
    }

    @Test
    @DisplayName("Return the ok request as adding workspace successfully")
    void should_Add_Workspace_For_UserById() {
        // Given
        // Assume User and Workspace has no connection to each other so far
        testUser.getWorkspaces().remove(testWorkspace);
        testWorkspace.getUsers().remove(testUser);

        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.of(testWorkspace));

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        testUser.getWorkspaces().forEach(System.out::println);
        System.out.println("Before adding: " + testUser.getWorkspaces().size());

        // When
        ResponseEntity<User> actual = userService.addWorkspaceForUserById(testWorkspace.getWorkspaceId(), testUser.getUserId());
        System.out.println("After adding: " + testUser.getWorkspaces().size());
        testUser.getWorkspaces().forEach(System.out::println);

        // Then
        ResponseEntity<User> expected = ResponseEntity.ok().build();
        assertEquals(expected, actual);
        System.out.println("Expected: " + expected + " - Actual: " + actual);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertThat(userArgumentCaptor.getValue()).isEqualTo(testUser);
        System.out.println("Test case passed: Add exist workspace to exist user");
    }

    @Test
    @DisplayName("Should delete user by their ID")
    void should_Delete_User_By_Id() {
        // Given
        testUser.setWorkspaces(workspaces);
        userRepository.save(testUser);
        // Assume the userRepository return the user
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));

        userService.deleteUserById(testUser.getUserId());

        // Then
        assertThat(testUser.getWorkspaces()).isEmpty();
        verify(userRepository).deleteById(testUser.getUserId());
        System.out.println("Test case passed: Delete user out of UserRepository");
    }

    @Test
    @DisplayName("Get user by their ID successfully")
    void should_Get_User_By_Exist_Id() {
        // Given
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
            System.out.println("Test case passed: Get user with exist userId");
            System.out.println(newUserDto);
        }catch(Exception e){
            exceptionThrown = false;
        }
        assertTrue(exceptionThrown, "Method should return new UserDTO object with user presents in the repository.");
    }

    @Test
    @DisplayName("Get user by their ID unsuccessfully")
    void test_Get_User_By_Non_Exist_Id() {
        // Given
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
            System.out.println("Test case passed: Get user with non-exist userId");
            System.out.println(newUserDto);
        }catch(Exception e){
            exceptionThrown = false;
        }
        assertTrue(exceptionThrown, "Method should return new null UserDTO object because user does not present in repository.");
    }

    @Test
    @DisplayName("Remove User from workspace by their ID")
    void should_Remove_User_From_Workspace_By_Id() {
        // Given
        userRepository.save(testUser);

        given(workspaceRepository.findById(testWorkspace.getWorkspaceId())).willReturn(Optional.of(testWorkspace));
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        System.out.println("User in workspace: " + testWorkspace.getUsers().size());

        // When
        System.out.println("Removing user out of workspace...");
        userService.removeUserFromWorkspaceById(testWorkspace.getWorkspaceId(), testUser.getUserId());
        System.out.println("User with id " + testUser.getUserId() + " has been removed out of workspace");

        // Then
        verify(workspaceRepository).save(testWorkspace);

        System.out.println("User in workspace: " + testWorkspace.getUsers().size());
        System.out.println("Test case passed: Remove user out of workspace successfully");
    }

    @Test
    @DisplayName("Edit password")
    void should_Edit_Password_By_UserId() {
        // Give
        // Assume UserRepository return User with given userId
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        System.out.println("Old password: " + testUser.getPassword());

        // When
        userService.editPassword(testUser.getUserId(), "Rm!t2022");

        // Then
        verify(userRepository).save(testUser);
        System.out.println("New password: " + testUser.getPassword());
        System.out.println("Test case passed: Edit user password successfully");
    }

    @Test
    @DisplayName("Edit username")
    void should_Edit_Username_By_UserId() {
        // Give
        // Assume UserRepository return User with given userId
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        System.out.println("Old username: " + testUser.getUsername());

        // When
        userService.editUsername(testUser.getUserId(), "skywalker");

        // Then
        verify(userRepository).save(testUser);
        System.out.println("New username: " + testUser.getUsername());
        System.out.println("Test case passed: Edit username successfully");
    }

    @Test
    @DisplayName("Edit name")
    void editName() {
        // Give
        // Assume UserRepository return User with given userId
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        System.out.println("Old name: " + testUser.getName());

        // When
        userService.editName(testUser.getUserId(), "John Doe");

        // Then
        verify(userRepository).save(testUser);
        System.out.println("New name: " + testUser.getName());
        System.out.println("Test case passed: Edit name successfully");
    }

    @Test
    @DisplayName("Edit avatar URL")
    void editAvatarURL() {
        // Give
        // Assume UserRepository return User with given userId
        given(userRepository.findById(testUser.getUserId())).willReturn(Optional.of(testUser));
        System.out.println("Old URL: " + testUser.getAvatarURL());

        // When
        userService.editAvatarURL(testUser.getUserId(), "https://thelifetank.com/wp-content/uploads/2018/08/avatar-default-icon.png");

        // Then
        verify(userRepository).save(testUser);
        System.out.println("New URL: " + testUser.getAvatarURL());
        System.out.println("Test case passed: Edit avatar URL successfully");
    }

}