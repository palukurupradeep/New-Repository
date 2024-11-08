package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.WorkQueueField;
@Repository
public interface WorkQueueRepository extends JpaRepository<WorkQueueField, Integer> {
	@Query("select w from WorkQueueField w where w.statusId=1 and w.displaySequence>0")
List<WorkQueueField> fetchWorkQueueFields();
}
