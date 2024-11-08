package com.shaw.claims.repo;

import com.shaw.claims.model.ApprovalLimitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalLimitTypeRepository extends JpaRepository<ApprovalLimitType,Integer> {
    @Query("SELECT alt.approvalLimitTypeId FROM ApprovalLimitType alt WHERE alt.approvalLimitTypeCode =:approvalLimitTypeCode AND alt.statusId = 1")
    int findApprovalLimitTypeIdByApprovalLimitTypeCode(@Param("approvalLimitTypeCode") String approvalLimitTypeCode);
}
