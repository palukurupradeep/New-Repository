package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimMerge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimMergeRepository extends JpaRepository<ClaimMerge, Integer> {
    @Query("SELECT c FROM ClaimMerge c WHERE c.parentClaimId =:parentClaimId AND c.mergedClaimId =:mergedClaimId and c.statusId = :statusId")
    ClaimMerge findByParentClaimIdAndMergedClaimId(Integer parentClaimId, Integer mergedClaimId,int statusId);
    @Query("SELECT c.mergedClaimId FROM ClaimMerge c WHERE c.parentClaimId = :parentClaimId AND c.statusId = :statusId")
    List<Integer> findMergedClaimIdByParentClaimId(@Param("parentClaimId") int parentClaimId, @Param("statusId") int statusId);
}
