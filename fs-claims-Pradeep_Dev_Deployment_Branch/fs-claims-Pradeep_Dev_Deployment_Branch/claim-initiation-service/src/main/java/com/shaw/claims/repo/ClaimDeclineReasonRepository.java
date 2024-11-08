package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.DeclineReason;

@Repository
public interface ClaimDeclineReasonRepository extends JpaRepository<DeclineReason, Integer> {

	List<DeclineReason> findByStatusId(int status);

}
