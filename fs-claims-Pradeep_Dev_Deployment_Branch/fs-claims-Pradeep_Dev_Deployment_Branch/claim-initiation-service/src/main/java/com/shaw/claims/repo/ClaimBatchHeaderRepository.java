package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimBatchHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimBatchHeaderRepository extends JpaRepository<ClaimBatchHeader,Integer> {
}
