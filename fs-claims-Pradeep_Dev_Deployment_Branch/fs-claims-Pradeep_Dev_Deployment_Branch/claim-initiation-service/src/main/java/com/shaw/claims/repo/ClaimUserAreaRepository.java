package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimUserArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimUserAreaRepository extends JpaRepository<ClaimUserArea, Integer> {
    @Query("SELECT c.userId FROM ClaimUserArea c WHERE c.claimAreaId = :claimAreaId AND c.areaTypeId = :areaTypeId")
    int findUserIdByClaimAreaIdAndAreaTypeId(int claimAreaId, int areaTypeId);
}
