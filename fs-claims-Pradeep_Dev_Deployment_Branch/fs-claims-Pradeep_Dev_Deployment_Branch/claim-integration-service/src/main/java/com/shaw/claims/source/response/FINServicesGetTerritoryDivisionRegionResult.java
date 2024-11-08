package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesGetTerritoryDivisionRegionResult {
	@JsonProperty("FinancialServices.Sales_Result")
	public String SalesResult;
	@JsonProperty("FinancialServices.sfdTerritory")
	public String sfdTerritory;
	@JsonProperty("FinancialServices.sfdDivTerritory")
	public String sfdDivTerritory;
	@JsonProperty("FinancialServices.sfdRgnTerritory")
	public String sfdRgnTerritory;
	@JsonProperty("FinancialServices.sfdFirstName")
	public String sfdFirstName;
	@JsonProperty("FinancialServices.sfdMiddleInitial")
	public String sfdMiddleInitial;
	@JsonProperty("FinancialServices.sfdLastName")
	public String sfdLastName;
	@JsonProperty("FinancialServices.sfdFormattedName")
	public String sfdFormattedName;
	@JsonProperty("FinancialServices.sfdOfficePhone")
	public String sfdOfficePhone;
	@JsonProperty("FinancialServices.sfdTerritoryEmail")
	public String sfdTerritoryEmail;
	@JsonProperty("FinancialServices.sfdDivision_oldregion")
	public String sfdDivisionOldregion;
	@JsonProperty("FinancialServices.sfdRegion_olddistrict")
	public String sfdRegionOlddistrict;
	@JsonProperty("FinancialServices.sfdType")
	public String sfdType;
	@JsonProperty("FinancialServices.sfdTypeDesc")
	public String sfdTypeDesc;
	@JsonProperty("FinancialServices.sfdTypeMapsTo")
	public String sfdTypeMapsTo;
	@JsonProperty("FinancialServices.sfdTerritoryStatus")
	public String sfdTerritoryStatus;
	@JsonProperty("FinancialServices.sfdTerritoryStatusDesc")
	public String sfdTerritoryStatusDesc;
}
