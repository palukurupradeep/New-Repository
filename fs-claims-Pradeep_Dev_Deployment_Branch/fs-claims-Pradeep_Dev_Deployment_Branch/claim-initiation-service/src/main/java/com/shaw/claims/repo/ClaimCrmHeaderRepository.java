package com.shaw.claims.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimCrmHeader;
@Repository
public interface ClaimCrmHeaderRepository extends JpaRepository<ClaimCrmHeader, Integer>{
	
	@Query(value = "SELECT NEXT VALUE FOR CLM.CRMNumberSequence", nativeQuery = true)
	Integer getNextCrmNumber();
	
	 @Query("SELECT crh FROM ClaimCrmHeader crh WHERE crh.claim.claimId =:claimId AND crh.crmNumber =:crmNumber")
	 Optional<ClaimCrmHeader> findClaimCrmHeaderByClaimIdAndCrmNumber(int claimId, String crmNumber);

}
