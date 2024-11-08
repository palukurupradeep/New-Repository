package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.Claim;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer> {
	@Query(value = "SELECT NEXT VALUE FOR CLM.ClaimNumberSequence", nativeQuery = true)
	Integer getNextClaimNumber();
	@Query("SELECT c FROM Claim c WHERE c.claimNumber = :claimNumber")
	List<Claim> findByClaimNumber(@Param("claimNumber") String claimNumber);
	@Query("SELECT c FROM Claim c WHERE c.customerNumber = :customerNumber AND c.claimStatusId=1")
	List<Claim> findByCustomerNumber(@Param("customerNumber") String customerNumber);
	@Query("SELECT c FROM Claim c WHERE c.claimUserId = :claimUserId")
	List<Claim> findByUserId(@Param("claimUserId") Integer claimUserId);
	@Query("SELECT c FROM Claim c WHERE c.claimUserId IN (:userIds) AND c.claimStatusId IN (1,3)")
	List<Claim> findAllByClaimUserId(@Param("userIds") List<Integer> userIds);
	@Query("SELECT c FROM Claim c WHERE c.claimId NOT IN (:parentClaimId) AND c.customerNumber = :customerNumber" +
			" AND c.claimStatusId IN (:claimStatusIds)")
	List<Claim> findByClaimIdAndCustomerNumberAndClaimStatusId(int parentClaimId, String customerNumber, List<Integer> claimStatusIds);
	@Query("SELECT c FROM Claim c JOIN c.claimDocuments cd " +
			"WHERE c.customerNumber = :customerNumber AND cd.documentNumber = :documentNumber AND" +
			" cd.claim.claimId = :claimId AND cd.isAssociate = false")
	Claim findByCustomerNumberAndDocumentNumberAndClaimId(@Param("customerNumber") String customerNumber,
												@Param("documentNumber") String documentNumber,
														  @Param("claimId") int claimId);
	Claim findByClaimId(int claimId);
	@Query("SELECT c.customerNumber, ca.fullName, COUNT(c) AS cnt, c.claimUserId FROM CustomerWatchlist cw JOIN Claim c on cw.customerNumber = c.customerNumber AND c.claimStatusId IN (1, 3) AND c.customerNumber!='' " +
			"JOIN ClaimAddress ca on c.claimId = ca.claim.claimId AND ca.addressTypeId = 14 AND cw.createdByUserId = :createdByUserId AND cw.statusId = 1 GROUP BY c.customerNumber, ca.fullName, c.claimUserId order by cnt desc")
	List<Object[]> getCustomerNumberCount(@Param("createdByUserId") Integer createdByUserId);

	@Query("SELECT c FROM Claim c JOIN c.claimDocuments cd " +
			"WHERE c.customerNumber = :customerNumber AND cd.documentNumber = :documentNumber AND " +
			"cd.claim.claimId = :claimId AND cd.isAssociate = true")
	Claim findByCustomerNumberAndDocumentNumberAndClaimIdAndIsAssociated(@Param("customerNumber") String customerNumber,
						   @Param("documentNumber") String documentNumber,
						   @Param("claimId") int claimId);

	@Query("SELECT c FROM Claim c WHERE c.region = :region AND c.territory = :territory AND c.claimCategory.claimCategoryCode = :claimCategoryCode")
    List<Claim> findByRegionAndTerritory(@Param("region") String region, @Param("territory") String territory, @Param("claimCategoryCode") String claimCategoryCode);
	@Query("SELECT c FROM Claim c WHERE c.region = :region AND c.territory = :territory AND c.division = :division AND c.claimCategory.claimCategoryCode = :claimCategoryCode")
	List<Claim> findByRegionAndTerritoryAndDivision(@Param("region") String region,@Param("territory") String territory,@Param("division") String division, @Param("claimCategoryCode") String claimCategoryCode);
	@Query("SELECT c FROM Claim c WHERE c.claimNumber = :claimNumber")
	Claim findClaimByClaimNumber(@Param("claimNumber") String claimNumber);
	@Query("SELECT c FROM Claim c WHERE c.claimNumber = :claimNumber ORDER BY c.createdDateTime DESC")
	List<Claim> findClaimByClaimNumberOrderByDate(@Param("claimNumber") String claimNumber);
}