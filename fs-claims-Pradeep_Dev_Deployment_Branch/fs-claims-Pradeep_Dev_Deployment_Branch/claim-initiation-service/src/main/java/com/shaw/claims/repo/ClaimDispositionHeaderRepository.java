package com.shaw.claims.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimDispositionHeader;

@Repository
public interface ClaimDispositionHeaderRepository extends JpaRepository<ClaimDispositionHeader, Integer> {
	@Query(value = "SELECT NEXT VALUE FOR CLM.DispositionNumberSequence", nativeQuery = true)
	Integer getNextDispositionNumber();
	
	 @Query("SELECT cdh FROM ClaimDispositionHeader cdh WHERE cdh.claim.claimId =:claimId AND cdh.dispositionNumber =:dispositionNumber")
	 Optional<ClaimDispositionHeader> findClaimDispositionHeaderByClaimIdAndDispositionNumber(int claimId, String dispositionNumber);
}
