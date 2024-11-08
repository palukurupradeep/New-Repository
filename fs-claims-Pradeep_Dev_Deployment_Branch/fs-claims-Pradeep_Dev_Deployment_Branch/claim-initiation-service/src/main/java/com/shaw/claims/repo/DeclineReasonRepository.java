package com.shaw.claims.repo;

import com.shaw.claims.model.DeclineReason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeclineReasonRepository extends JpaRepository<DeclineReason,Integer> {
    @Query("SELECT d.declineReasonId FROM DeclineReason d WHERE d.declineReasonCode =:declineReasonCode")
    int findDeclineReasonIdByDeclineReasonCode(@Param("declineReasonCode") String declineReasonCode);
}
