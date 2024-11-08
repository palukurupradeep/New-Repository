package com.shaw.claims.repo;

import com.shaw.claims.dto.ClaimRateDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.ClaimRateDetail;

@Repository
public interface ClaimRateDetailRepository extends JpaRepository<ClaimRateDetail, Integer> {
	@Query("SELECT new com.shaw.claims.dto.ClaimRateDTO(cr.overallRate, cr.deffectRate, cr.adminRate)"
			+ " from ClaimRateDetail cr where cr.customerNumber=:customerNumber and cr.claimReasonId=1 and cr.styleNumber='ALL'")
	ClaimRateDTO getClaimRatesOfCustomer(String customerNumber);
	@Query("SELECT new com.shaw.claims.dto.ClaimRateDTO(SUM(cr.overallSalesAmount), SUM(cr.overallCreditAmount), SUM(cr.deffectCreditAmount), SUM(cr.adminCreditAmount))"
			+ " from ClaimRateDetail cr where cr.customerNumber=:customerNumber and cr.claimReasonId=1 and cr.styleNumber='ALL'")
	ClaimRateDTO getClaimDollarAmountOfCustomer(String customerNumber);
	@Query("SELECT new com.shaw.claims.dto.ClaimRateDTO(cr.overallRate, cr.deffectRate, cr.adminRate, cr.overallSalesAmount, cr.overallCreditAmount, cr.deffectCreditAmount, cr.adminCreditAmount)"
			+ " from ClaimRateDetail cr where cr.customerNumber='SHAW' and cr.claimReasonId=1 and cr.styleNumber='ALL'")
	ClaimRateDTO getClaimRatesAndDollarAmountOfShaw();
	@Query("SELECT new com.shaw.claims.dto.ClaimRateDTO(SUM(cr.overallSalesAmount), SUM(cr.overallCreditAmount), SUM(cr.deffectCreditAmount), SUM(cr.adminCreditAmount))"
			+ " from ClaimRateDetail cr where cr.commonCustomerNumber=:commonCustomerNumber and cr.claimReasonId=1 and cr.styleNumber='ALL'")
	ClaimRateDTO getClaimDollarAmountOfCumulative(String commonCustomerNumber);
	@Query("SELECT new com.shaw.claims.dto.ClaimRateDTO(SUM(cr.overallSalesAmount), SUM(cr.overallCreditAmount), SUM(cr.deffectCreditAmount), SUM(cr.adminCreditAmount))"
			+ " from ClaimRateDetail cr where cr.buyingGroup=:buyingGroup and cr.claimReasonId=1 and cr.styleNumber='ALL'")
	ClaimRateDTO getClaimDollarAmountOfBuyingGroup(String buyingGroup);
}
