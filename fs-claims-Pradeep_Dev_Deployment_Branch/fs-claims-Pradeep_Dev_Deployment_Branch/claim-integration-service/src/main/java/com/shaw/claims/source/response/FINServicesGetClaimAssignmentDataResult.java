package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesGetClaimAssignmentDataResult {
	@JsonProperty("FinancialServices.CustomerNumber")
	public String CustomerNumber;
	@JsonProperty("FinancialServices.CustomerName")
	public String CustomerName;
	@JsonProperty("FinancialServices.ClaimsArea")
	public String ClaimsArea;
	@JsonProperty("FinancialServices.ClaimsAreaName")
	public String ClaimsAreaName;
	@JsonProperty("FinancialServices.BusinessSegment")
	public String BusinessSegment;
	@JsonProperty("FinancialServices.BusinessSegmentName")
	public String BusinessSegmentName;
	@JsonProperty("FinancialServices.HomeCenter")
	public String HomeCenter;
	@JsonProperty("FinancialServices.AlignedAccount")
	public String AlignedAccount;
	@JsonProperty("FinancialServices.CustomerReferenceNumber")
	public String CustomerReferenceNumber;
	@JsonProperty("FinancialServices.CustomerReferenceNumberDesc")
	public String CustomerReferenceNumberDesc;
	@JsonProperty("FinancialServices.CCAAccount")
	public String ccaAccount;
}
