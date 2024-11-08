package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimBatchDetail;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimBatchDetailRepository extends JpaRepository<ClaimBatchDetail,Integer> {
	@Query("select c from ClaimBatchDetail c where c.claimBatchHeader.claimBatchHeaderId=:claimBatchHeaderId")
	List<ClaimBatchDetail> findClaimBatchDetailByclaimBatchHeaderId(Integer claimBatchHeaderId);
}
