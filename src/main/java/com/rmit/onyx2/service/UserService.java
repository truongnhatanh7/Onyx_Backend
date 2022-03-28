package com.rmit.onyx2.service;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.UserDTO;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.UserRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private WorkspaceRepository workspaceRepository;
    private WorkspaceListRepository workspaceListRepository;
    private TaskRepository taskRepository;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    public UserService(UserRepository userRepository,
                       WorkspaceRepository workspaceRepository,
                       WorkspaceListRepository workspaceListRepository,
                       TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
        this.workspaceListRepository = workspaceListRepository;
        this.taskRepository = taskRepository;
    }

    public List<UserDTO> getAllUsers() {
        List<User> temp = userRepository.findAll();
        List<UserDTO> getResult = new ArrayList<>();
        for (User user : temp) {
            getResult.add(new UserDTO(user));
        }
        return getResult;
    }

    public ResponseEntity<User> addUser(User user) {
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<User> addWorkspaceForUserById(Long workspaceId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (user.isPresent() && workspace.isPresent()) {
            user.get().getWorkspaces().add(workspace.get());
            userRepository.save(user.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //A function to edit user
    public ResponseEntity<User> editUser(User user) {
        Optional<User> userTmp = userRepository.findById(user.getUserId());
        if (userTmp.isPresent()) {
            userRepository.save(user);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    public void deleteUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // Remove orphan workspaces
//            user.get().getWorkspaces().stream()
//                    .filter(w -> w.getUsers().size() == 1)
//                    .forEach(w -> workspaceService.deleteWorkspaceById(w.getWorkspaceId()));
            user.get().getWorkspaces().clear();

            // Remove user in workspaces
            for (Workspace workspace : workspaceRepository.findAll()) {
                workspace.getUsers().stream()
                        .filter(u -> u.getUserId().equals(userId))
                        .forEach(u -> {
                            workspace.getUsers().remove(u);
                            if (workspace.getUsers().isEmpty()) {
                                workspaceService.deleteWorkspaceById(workspace.getWorkspaceId());
                            }
                        });
            }
            userRepository.deleteById(userId);
        }
    }

    public ResponseEntity<User> removeUserFromWorkspaceById(Long workspaceId, Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // Remove orphan workspaces
//            user.get().getWorkspaces().stream()
//                    .filter(w -> w.getUsers().size() == 1)
//                    .forEach(w -> workspaceService.deleteWorkspaceById(w.getWorkspaceId()));
            user.get().getWorkspaces().clear();

            // Remove user in workspaces
            Optional<Workspace> workspaceOptional = workspaceRepository.findById(workspaceId);

            if (workspaceOptional.isPresent()) {
                Workspace workspace = workspaceOptional.get();
                workspace.getUsers().stream()
                        .filter(u -> u.getUserId().equals(userId))
                        .forEach(u -> {
                            workspace.getUsers().remove(u);
                            if (workspace.getUsers().isEmpty()) {
                                workspaceService.deleteWorkspaceById(workspace.getWorkspaceId());
                                ResponseEntity.ok().build();
                            }
                        });
            }

        }

        return ResponseEntity.badRequest().build();
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return new UserDTO(user.get());
        }
        return new UserDTO();
    }
}
