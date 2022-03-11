package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.Workspace;
//import com.rmit.onyx2.model.WorkspaceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    @Modifying
    @Query("delete from Workspace w where w.workspaceId=:workspaceId")
    void deleteById(Long workspaceId);
}
