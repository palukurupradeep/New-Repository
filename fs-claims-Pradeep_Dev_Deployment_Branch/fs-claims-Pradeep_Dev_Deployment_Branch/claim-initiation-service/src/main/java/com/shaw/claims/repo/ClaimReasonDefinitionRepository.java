package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimReasonDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimReasonDefinitionRepository extends JpaRepository<ClaimReasonDefinition, Integer> {
    @Query("SELECT cr FROM ClaimReasonDefinition cr JOIN cr.claimCategory cc ON cc.claimCategoryId = :categoryId AND cr.status.statusId = 1")
    List<ClaimReasonDefinition> getReasonCodeByCategory(@Param("categoryId") Integer categoryId);
    @Query("SELECT cr FROM ClaimReasonDefinition cr WHERE cr.claimReasonCode =:reasonCode")
    ClaimReasonDefinition findByClaimReasonCode(@Param("reasonCode") String reasonCode);
    @Query("SELECT cr FROM ClaimReasonDefinition cr WHERE cr.claimReasonId != 1 AND cr.status.statusId = 1 ORDER BY cr.claimReasonCode")
    List<ClaimReasonDefinition> getAllReasonCodes();
    ClaimReasonDefinition findByClaimReasonId(Integer claimReasonId);
    List<ClaimReasonDefinition> findByclaimReasonCodeIn(List<String> reasonCodeList);
}