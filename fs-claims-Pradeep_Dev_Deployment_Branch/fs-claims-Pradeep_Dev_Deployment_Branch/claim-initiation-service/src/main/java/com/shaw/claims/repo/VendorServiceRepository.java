package com.shaw.claims.repo;

import com.shaw.claims.model.VendorService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorServiceRepository extends JpaRepository<VendorService,Integer> {
    List<VendorService> findByClaimVendor_ClaimVendorIdAndStatusId(@Param("claimVendorId") Integer claimVendorId,@Param("active") int active);
}