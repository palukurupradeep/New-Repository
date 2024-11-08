package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FINServicesGetCrmReasonsResult {
	@JsonProperty("FinancialServices.Code")
	public String Code;
	@JsonProperty("FinancialServices.Description")
	public String Description;
	@JsonProperty("FinancialServices.ProdRestrict")
	public String ProdRestrict;
	@JsonProperty("FinancialServices.ApprRestrict")
	public String ApprRestrict;
	@JsonProperty("FinancialServices.SLCORestrict")
	public String SlcoRestrict;
	@JsonProperty("FinancialServices.XLATRestrict")
	public String XlatRestrict;
	@JsonProperty("FinancialServices.OverrideRestrict")
	public String OverrideRestrict;

}
