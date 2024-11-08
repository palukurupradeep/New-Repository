package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimDetailRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimDetailRecordRepository extends JpaRepository<ClaimDetailRecord,Integer> {
}
