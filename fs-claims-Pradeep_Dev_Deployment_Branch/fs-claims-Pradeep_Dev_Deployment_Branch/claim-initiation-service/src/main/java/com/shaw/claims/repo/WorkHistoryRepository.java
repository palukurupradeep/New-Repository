package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.Claim;
import com.shaw.claims.model.WorkHistory;

@Repository
public interface WorkHistoryRepository extends JpaRepository<WorkHistory, Integer> {

	List<WorkHistory> findByCreatedByUserId(int usersIds);

	Optional<WorkHistory> findByClaim(Claim claim);

	Optional<WorkHistory> findByClaimAndModifiedByUserId(Claim claim, Integer modifiedUserId);
}
