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
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {
    private final SseService sseService;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceListRepository workspaceListRepository;
    private final TaskRepository taskRepository;
    @Autowired
    public JavaMailSender emailSender;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WorkspaceService workspaceService;

    @Autowired
    public UserService(SseService sseService, UserRepository userRepository,
                       WorkspaceRepository workspaceRepository,
                       WorkspaceListRepository workspaceListRepository,
                       TaskRepository taskRepository) {
        this.sseService = sseService;
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

    public UserDTO addUser(User user) {
        Long tempId = userRepository.save(user).getUserId();
        return new UserDTO(userRepository.findById(tempId).get());
    }


    public ResponseEntity<User> addWorkspaceForUserById(Long workspaceId, Long userId) {
        //Retrieving workspace
        Optional<User> user = userRepository.findById(userId);
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (user.isPresent() && workspace.isPresent()) {
            for (User u : workspace.get().getUsers()) {
                //If user is workspace owner
                if (Objects.equals(u.getUserId(), user.get().getUserId())) {
                    return ResponseEntity.badRequest().build();
                }
            }
            //Normal user
            user.get().getWorkspaces().add(workspace.get());
            userRepository.save(user.get());
            try {
                //Braod cast change event
                SseEmitter sseEmitter = new SseEmitter();
                sseService.addEmitter(sseEmitter);
                sseService.doNotifyDashboard("addWorkspaceForUserById");

            } catch (Exception e) {
                System.out.println("Fail");
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //A function to edit user
    @Transactional
    public ResponseEntity<User> editUser(User user) {
        String hsql = "update User u set u.name =:name, u.username =:user_name, u.password =:password where u.userId =: userId";
        Query query = entityManager.createQuery(hsql);
        query.setParameter("userId",user.getUserId());
        query.setParameter("name",user.getName());
        query.setParameter("user_name",user.getName());
        query.setParameter("password",user.getPassword());
        entityManager.flush();
        int result = query.executeUpdate();
        if (result != 0) {
           return ResponseEntity.ok().build();
        }
        return  ResponseEntity.badRequest().build();
    }

    public void deleteUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // Remove orphan workspaces
//            user.get().getWorkspaces().stream()
//                    .filter(w -> w.getUsers().size() == 1)
//                    .forEach(w -> workspaceService.deleteWorkspaceById(w.getWorkspaceId()));
            user.get().getWorkspaces().clear();
            userRepository.flush();

            // Remove user in workspaces
//            for (Workspace workspace : workspaceRepository.findAll()) {
//                workspace.getUsers().stream()
//                        .filter(u -> u.getUserId().equals(userId))
//                        .forEach(u -> {
//                            workspace.getUsers().remove(u);
//                            if (workspace.getUsers().isEmpty()) {
//                                workspaceService.deleteWorkspaceById(workspace.getWorkspaceId());
//                            }
//                        });
//            }
            userRepository.deleteById(userId);
        }
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.map(UserDTO::new).orElseGet(UserDTO::new);
    }

    public void removeUserFromWorkspaceById(Long workspaceId, Long userId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        Optional<User> user = userRepository.findById(userId);
        if (workspace.isPresent() && user.isPresent()) {
            //Modify the workspace list and user list in both workspaceRepository and userRepository.
            workspace.get().getUsers().remove(user.get());
            user.get().getWorkspaces().remove(workspace.get());
            workspaceRepository.save(workspace.get());
            userRepository.save(user.get());
            try {
                SseEmitter sseEmitter = new SseEmitter();
                sseService.addEmitter(sseEmitter);
                sseService.doNotifyDashboard("removeUserFromWorkspaceById");

            } catch (Exception e) {
                System.out.println("Fail");
            }
        }
    }

    //Updating account functionalities
    public void editPassword(Long userId, String newPassword) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setPassword(newPassword);
            userRepository.save(user.get());
        }
    }

    public void editUsername(Long userId, String newUsername) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setUsername(newUsername);
            userRepository.save(user.get());
        }
    }

    public void editName(Long userId, String newName) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setName(newName);
            userRepository.save(user.get());
        }
    }

    public void editAvatarURL(Long userId, String newAvatarURL) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setAvatarURL(newAvatarURL);
            userRepository.save(user.get());
        }
    }
}
