package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimRgaHeader;

@Repository
public interface ClaimRgaHeaderRepository extends JpaRepository<ClaimRgaHeader, Integer> {

	
	 @Query("SELECT crh FROM ClaimRgaHeader crh WHERE crh.claim.claimId =:claimId")
	 List<ClaimRgaHeader> findClaimRgaHeaderByClaimId(int claimId);
	 
	 @Query("SELECT crh FROM ClaimRgaHeader crh WHERE crh.claim.claimId =:claimId AND crh.rgaNumber =:rgaNumber")
	 Optional<ClaimRgaHeader> findClaimRgaHeaderByClaimIdAndRgaNumber(int claimId, String rgaNumber);

	 
	 @Query("SELECT crh FROM ClaimRgaHeader crh WHERE crh.rgaNumber =:rgaNumber")
	 Optional<ClaimRgaHeader> findClaimRgaHeaderByRgaNumber(String rgaNumber);


	@Query("SELECT crh FROM ClaimRgaHeader crh WHERE crh.rDCLocationId =:locationId")
	List<ClaimRgaHeader> findByLocationId(int locationId);

	@Query("SELECT crh FROM ClaimRgaHeader crh WHERE crh.rDCLocationId =:locationId AND crh.rgaNumber =:rgaNumber")
	List<ClaimRgaHeader> findByLocationIdAndRgaNumber(int locationId, String rgaNumber);
	
	@Query(value = "SELECT NEXT VALUE FOR CLM.RGANumberSequence", nativeQuery = true)
	Integer getNextRgaNumber();
	
	@Query("SELECT crh FROM ClaimRgaHeader crh WHERE crh.claim.claimId =:claimId AND crh.rgaStatusId = 1")
	List<ClaimRgaHeader> findByClaimIdAndRgaStatusId(int claimId);

}
