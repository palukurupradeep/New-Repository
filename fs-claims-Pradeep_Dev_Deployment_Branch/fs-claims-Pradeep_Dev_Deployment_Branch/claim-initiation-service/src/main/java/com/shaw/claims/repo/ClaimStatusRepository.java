package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClaimStatusRepository extends JpaRepository<ClaimStatus, Integer> {
    @Query("SELECT s.claimStatusId FROM ClaimStatus s WHERE s.claimStatusCode IN (:claimStatusCodes)")
    List<Integer> findClaimStatusIdByClaimStatusCode(@Param("claimStatusCodes") List<String> claimStatusCodes);

    @Query("SELECT s.claimStatusDescription FROM ClaimStatus s WHERE s.claimStatusId = :claimStatusId")
    String findClaimStatusDescriptionByClaimStatusId(@Param("claimStatusId") int claimStatusId);
    
    @Query("SELECT s FROM ClaimStatus s WHERE s.claimStatusCode = :claimStatusCode")
    ClaimStatus findClaimStatusByClaimStatusCode(@Param("claimStatusCode") String claimStatusCode);

}
