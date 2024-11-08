package com.shaw.claims.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.AuditHistory;

import java.util.List;

@Repository
public interface AuditHistoryRepository  extends JpaRepository<AuditHistory, Integer>{
    @Query("select a from AuditHistory a where a.claim.claimId=:claimId AND a.statusId=1")
    List<AuditHistory> fetchAuditHistoryByClaimId(Integer claimId);
}
