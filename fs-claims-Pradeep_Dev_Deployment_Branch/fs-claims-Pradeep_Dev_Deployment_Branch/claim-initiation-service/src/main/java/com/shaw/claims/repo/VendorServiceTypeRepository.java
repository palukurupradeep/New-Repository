package com.shaw.claims.repo;

import com.shaw.claims.model.VendorServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorServiceTypeRepository extends JpaRepository<VendorServiceType,Integer> {
    List<VendorServiceType> findByClaimVendor_ClaimVendorIdAndStatusId(@Param("claimVendorId") Integer claimVendorId,@Param("active") int active);
}