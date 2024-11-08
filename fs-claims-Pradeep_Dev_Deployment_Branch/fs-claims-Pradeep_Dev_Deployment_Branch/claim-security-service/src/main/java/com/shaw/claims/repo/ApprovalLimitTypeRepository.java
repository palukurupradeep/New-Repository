package com.shaw.claims.repo;

import com.shaw.claims.model.ApprovalLimitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalLimitTypeRepository extends JpaRepository<ApprovalLimitType,Integer> {
    @Query("SELECT alt FROM ApprovalLimitType alt WHERE alt.statusId = 1")
    List<ApprovalLimitType> findAllActiveApprovalLimitType();
    Optional<ApprovalLimitType> findByApprovalLimitTypeCode(String approvalLimitTypeCode);
}
