package com.rmit.onyx2.service;

import com.rmit.onyx2.model.Task;
import com.rmit.onyx2.model.Workspace;
import com.rmit.onyx2.model.WorkspaceList;
import com.rmit.onyx2.model.WorkspaceListDTO;
import com.rmit.onyx2.repository.TaskRepository;
import com.rmit.onyx2.repository.WorkspaceListRepository;
import com.rmit.onyx2.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class WorkspaceListService {
    private final SseService sseService;
    private final WorkspaceListRepository workspaceListRepository;
    private final WorkspaceRepository workspaceRepository;
    private final TaskRepository taskRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public WorkspaceListService(SseService sseService, WorkspaceListRepository workspaceListRepository, WorkspaceRepository workspaceRepository, TaskRepository taskRepository) {
        this.sseService = sseService;
        this.workspaceListRepository = workspaceListRepository;
        this.workspaceRepository = workspaceRepository;
        this.taskRepository = taskRepository;
    }

    public List<WorkspaceList> getWorkspaceListByWorkspaceId(Long id) {
        Optional<Workspace> workspace = workspaceRepository.findById(id);
        if (workspace.isPresent()) {
            workspace.get().getWorkspaceLists();
            List<WorkspaceList> temp = new ArrayList<>();
            temp.addAll(workspace.get().getWorkspaceLists());
//            for (WorkspaceList workspaceList : workspace.get().getWorkspaceLists()) {
//                temp.add(workspaceList);
//            }
            temp.sort(Comparator.comparing(WorkspaceList::getListId));
            return temp;
        }
        return null;
    }

    public WorkspaceListDTO addWorkspaceListByWorkspaceId(Long workspaceId, WorkspaceList workspaceList) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        if (workspace.isPresent()) {
            workspaceList.setWorkspace(workspace.get());
            workspaceListRepository.save(workspaceList);
            List<WorkspaceList> temp = workspaceListRepository.findAll();
            try {
                SseEmitter sseEmitter = new SseEmitter();
                sseService.addEmitter(sseEmitter);
                sseService.doNotify("addWorkspaceListByWorkspaceId");

            } catch (Exception e) {
                System.out.println("Fail");
            }
            if (temp.size() > 0) {
                return new WorkspaceListDTO(temp.get(temp.size() - 1));
            } else {
                return new WorkspaceListDTO();
            }
        }
        return new WorkspaceListDTO();
    }

    //A class to edit the information of workspace
    @Transactional
   public Integer editWorkspaceList(WorkspaceList workspaceList, Long workspaceId) {
       String hsql = "update WorkspaceList w set w.name =:nameList where w.listId =: listId and w.workspace.workspaceId =: workspaceId";
       Query query = entityManager.createQuery(hsql);
       query.setParameter("listId",workspaceList.getListId());
       query.setParameter("nameList",workspaceList.getName());
       query.setParameter("workspaceId",workspaceId);
       entityManager.flush();
       Integer result = query.executeUpdate();
       entityManager.clear();
        try {
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("editWorkspaceList");

        } catch (Exception e) {
            System.out.println("Fail");
        }
       return result;
   }

    public void deleteWorkspaceListById(Long workspaceListId) {
        Optional<WorkspaceList> temp = workspaceListRepository.findById(workspaceListId);
        if (temp.isPresent()) {
            for (Task t : temp.get().getTasks()) {
                taskRepository.deleteById(t.getTaskId());
            }
            workspaceListRepository.deleteById(workspaceListId);
        }
        try {
            SseEmitter sseEmitter = new SseEmitter();
            sseService.addEmitter(sseEmitter);
            sseService.doNotify("deleteWorkspaceListById");

        } catch (Exception e) {
            System.out.println("Fail");
        }
    }

}
