package com.shaw.claims.repo;

import com.shaw.claims.model.UserApprovalLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface UserApprovalLimitRepository extends JpaRepository<UserApprovalLimit,Integer> {
    @Query("SELECT ult.approvalLimit FROM UserApprovalLimit ult WHERE ult.userId =:userId AND ult.approvalLimitTypeId =:approvalLimitTypeId AND ult.statusId = 1")
    BigDecimal findApprovalLimitByUserIdAndApprovalLimitTypeId(@Param("userId") int userId,@Param("approvalLimitTypeId") int approvalLimitTypeId);
}
