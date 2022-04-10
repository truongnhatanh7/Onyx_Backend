package com.rmit.onyx2.repository;

import com.rmit.onyx2.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Modifying
    @Query("delete from Task t where t.taskId=:taskId")
    void deleteById(Long taskId);

    @Modifying
    @Query("update Task t set t.pos=:pos where t.taskId=:taskId")
    void updatePos(@Param(value = "taskId") Long taskId, @Param(value = "pos") Integer pos);
}
