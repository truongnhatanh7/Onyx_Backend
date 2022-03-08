package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.WorkspaceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceListRepository extends JpaRepository<WorkspaceList, Long> {
}
