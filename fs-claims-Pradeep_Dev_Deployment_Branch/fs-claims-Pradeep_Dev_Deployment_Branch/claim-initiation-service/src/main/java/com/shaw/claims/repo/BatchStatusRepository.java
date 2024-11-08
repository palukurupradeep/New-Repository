package com.shaw.claims.repo;

import com.shaw.claims.model.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchStatusRepository extends JpaRepository<BatchStatus,Integer> {

    BatchStatus findByBatchStatusCode(String batchStatusCode);
}
