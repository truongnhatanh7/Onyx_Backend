package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.WorkspaceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceListRepository extends JpaRepository<WorkspaceList, Long> {
    @Modifying
    @Query("delete from WorkspaceList wl where wl.listId=:workspaceListId")
    void deleteById(Long workspaceListId);
}
