package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimRestockFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ClaimRestockFeeRepository extends JpaRepository<ClaimRestockFee,Integer> {
    @Query("SELECT crf.rate FROM ClaimRestockFee crf WHERE crf.claimRestockFeeDescription = :claimRestockFeeDescription")
    BigDecimal findRateByClaimRestockFeeDescription(@Param("claimRestockFeeDescription") String claimRestockFeeDescription);
}
