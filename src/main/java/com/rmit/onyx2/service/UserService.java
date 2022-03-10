package com.rmit.onyx2.service;

import com.rmit.onyx2.model.User;
import com.rmit.onyx2.model.UserDTO;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.repository.UserRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private WorkspaceRepository workspaceRepository;

    @Autowired
    public UserService(UserRepository userRepository, WorkspaceRepository workspaceRepository) {
        this.userRepository = userRepository;
        this.workspaceRepository = workspaceRepository;
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

    public ResponseEntity<User> deleteUserById(long userId){
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User tmpUser = user.get();
//            //Loop through user list of work spaces to remove user
//            for (Workspace workspace : tmpUser.getWorkspaces()) {
//                if (workspace.getUsers().contains(tmpUser)) {
//                    workspace.removeUser(tmpUser);
//                }
//            }
//            userRepository.save(tmpUser);
            userRepository.delete(tmpUser);
            return ResponseEntity.ok().build();
        }
       return ResponseEntity.badRequest().build();
    }
}
