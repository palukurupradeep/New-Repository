package com.shaw.claims.repo;

import com.shaw.claims.model.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, Integer> {
    @Query("SELECT w.workStatusId FROM WorkStatus w WHERE w.workStatusCode =:workStatusCode")
    int findWorkStatusIdByWorkStatusCode(@Param("workStatusCode") String workStatusCode);
    @Query("SELECT w FROM WorkStatus w WHERE w.workStatusCode =:workStatusCode")
    WorkStatus findByWorkStatusCode(String workStatusCode);
    @Query("SELECT w.workStatusDescription FROM WorkStatus w WHERE w.workStatusId =:workStatusId")
    String findworkStatusDescriptionByWorkStatusId(@Param("workStatusId") int workStatusId);
   

}
