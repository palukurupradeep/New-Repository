package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimAreaRepository extends JpaRepository<ClaimArea,Integer> {
    @Query("SELECT c.claimAreaId FROM ClaimArea c WHERE c.claimAreaCode =:claimAreaCode")
    int findClaimAreaIdByClaimAreaCode(@Param("claimAreaCode") String claimAreaCode);

	ClaimArea findByClaimAreaId(int claimAreaId);
}
