package com.shaw.claims.repo;

import com.shaw.claims.model.ClaimAddress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClaimAddressRepository extends JpaRepository<ClaimAddress,Integer> {

	List<ClaimAddress> findByClaim_ClaimId(String claimId);

    List<ClaimAddress> findByAddressTypeId(int addressTypeId);
}
