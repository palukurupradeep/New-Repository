package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shaw.claims.model.EndUserInformation;

public interface EndUserInformationRepository extends JpaRepository<EndUserInformation, Integer> {
	@Query("SELECT e FROM EndUserInformation e WHERE e.firstName = :firstName AND e.lastName = :lastName AND e.homePhoneNumber = :phoneNumber AND e.addressLine1 =:address")
	List<EndUserInformation> findByEndUserData(String firstName, String lastName, String phoneNumber, String address);
	@Query("SELECT c FROM EndUserInformation c WHERE c.claim.claimId = :claimId")
	List<EndUserInformation> findByClaimId(@Param("claimId") Integer claimId);
}
