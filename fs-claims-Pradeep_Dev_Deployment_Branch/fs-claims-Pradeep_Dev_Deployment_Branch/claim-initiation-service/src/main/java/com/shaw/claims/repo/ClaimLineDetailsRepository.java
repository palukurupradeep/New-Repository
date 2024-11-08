package com.shaw.claims.repo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimLineDetail;

@Repository
public interface ClaimLineDetailsRepository extends JpaRepository<ClaimLineDetail,Integer> {
    @Query("SELECT cld.claimDocument.claimDocumentId FROM ClaimLineDetail cld WHERE cld.claim.claimId = :parentClaimId AND cld.lineSource.lineSourceId =:lineSourceId")
    Set<Integer> findByClaimIdAndLineSourceId(int parentClaimId, int lineSourceId);

	Optional<ClaimLineDetail> findByClaim_ClaimIdAndClaimReasonDefinition_claimReasonCode(Integer claimId,
			String reasonCode);
	 @Query("SELECT cld FROM ClaimLineDetail cld WHERE cld.claimLineId = :id AND cld.detailType.detailTypeId != 20")
	    ClaimLineDetail findByLineIdIgnoreReturnFreight(@Param("id") Integer id);

	    @Query("SELECT cld FROM ClaimLineDetail cld WHERE cld.claimLineId = :id AND cld.detailType.detailTypeId NOT IN (5,9)")
	    ClaimLineDetail findByLineIdIgnoreFreightAndMiceCharges(@Param("id") Integer id);

	List<ClaimLineDetail> findByClaim_ClaimIdAndDetailType_DetailTypeId(Integer claimId, int detailTypeId);
    int findClaimDocument_ClaimDocumentIdByClaimLineId(@Param("claimLineId") Integer claimLineId);
	List<ClaimLineDetail> findByClaim_ClaimId(Integer claimId);

	ClaimLineDetail findByClaimLineId(int claimLineId);
	@Query("SELECT cld FROM ClaimLineDetail cld WHERE cld.claim.claimId = :claimId AND cld.claimDocument.claimDocumentId =:documentId and statusId=1")
	List<ClaimLineDetail> findByClaimIdAndDocumentId(int claimId, int documentId);

}
