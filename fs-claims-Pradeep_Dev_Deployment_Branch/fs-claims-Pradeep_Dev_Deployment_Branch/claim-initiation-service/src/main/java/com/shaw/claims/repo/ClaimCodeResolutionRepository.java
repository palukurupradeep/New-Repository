package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimCodeResolution;

@Repository
public interface ClaimCodeResolutionRepository extends JpaRepository<ClaimCodeResolution, Integer> {

	Optional<ClaimCodeResolution> findByCodeResolutionId(int codeResolutionId);
	@Query("select c from ClaimCodeResolution c where c.claimResolution.resolutionId=:resolutionId")
	List<ClaimCodeResolution> findClaimCodeResolutionWithresolutionId(Integer resolutionId);
	@Query("select c from ClaimCodeResolution c where c.claimReasonDefinition.claimReasonId=:reasonId and c.status=:active")
	List<ClaimCodeResolution> findActiveClaimCodeResolutionByReasonId(int active, Integer reasonId);
	
	@Query("select distinct(c.claimResolution.resolutionId) from ClaimCodeResolution c where c.claimReasonDefinition.claimReasonId=:reasonId")
	List<Integer> findClaimCodeResolutionByReasonId(Integer reasonId);
}
