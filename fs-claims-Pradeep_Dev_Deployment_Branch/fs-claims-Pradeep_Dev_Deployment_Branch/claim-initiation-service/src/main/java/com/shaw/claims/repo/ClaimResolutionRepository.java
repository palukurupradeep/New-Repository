package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimResolution;

@Repository
public interface ClaimResolutionRepository extends JpaRepository<ClaimResolution, Integer> {

	ClaimResolution findByResolutionId(Integer resolutionId);

	Optional<ClaimResolution> findByResolutionName(String resolutionName);
	@Query("select c from ClaimResolution c where c.status=1 ORDER BY c.modifiedDateTime DESC")
	List<ClaimResolution> findAllClaimResolution();

}
