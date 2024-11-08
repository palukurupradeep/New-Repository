package com.shaw.claims.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.TraceTask;

@Repository
public interface TraceTaskRepository extends JpaRepository<TraceTask,Integer> {
	
	List<TraceTask> findByClaim_ClaimId(int claimId);

}
