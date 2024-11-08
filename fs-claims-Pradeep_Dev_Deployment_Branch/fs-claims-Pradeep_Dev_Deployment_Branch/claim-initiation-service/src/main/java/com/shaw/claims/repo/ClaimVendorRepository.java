package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimVendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimVendorRepository extends JpaRepository<ClaimVendor,Integer> {
    @Query(value = "SELECT NEXT VALUE FOR CLM.VendorNumberSequence", nativeQuery = true)
    Integer getNextClaimVendorNumber();

    ClaimVendor findByClaimVendorNumber(@Param("claimVendorNumber") String claimVendorNumber);
    @Query("SELECT cv.claimVendorNumber FROM ClaimVendor cv WHERE cv.claimVendorId =:claimVendorId")
    String findClaimVendorNumberByClaimVendorId(@Param("claimVendorId") int claimVendorId);
}