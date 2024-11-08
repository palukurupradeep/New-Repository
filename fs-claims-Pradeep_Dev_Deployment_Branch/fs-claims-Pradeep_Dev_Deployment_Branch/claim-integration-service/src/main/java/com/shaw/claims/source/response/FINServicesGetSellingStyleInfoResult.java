package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesGetSellingStyleInfoResult {
	
	@JsonProperty("FinancialServices.SellStyleNumber")
	public String SellStyleNumber;
	
	@JsonProperty("FinancialServices.Name")
	public String Name;
	
	@JsonProperty("FinancialServices.ProductCode")
	public String ProductCode;
	
	@JsonProperty("FinancialServices.ProductCodeDescription")
	public String ProductCodeDescription;
	
	@JsonProperty("FinancialServices.AbbreviatedName")
	public String AbbreviatedName;
	
	@JsonProperty("FinancialServices.ProductTypeCode")
	public String ProductTypeCode;
	
	@JsonProperty("FinancialServices.PrimaryInventoryStyleNbr")
	public String PrimaryInventoryStyleNbr;
	
	@JsonProperty("FinancialServices.SellingCompany")
	public String SellingCompany;
	
	@JsonProperty("FinancialServices.CustomerReferenceNbr")
	public String CustomerReferenceNbr;
	
	@JsonProperty("FinancialServices.StatusCode")
	public String StatusCode;

	@JsonProperty("FinancialServices.SellColorNbr")
	public String SellColorNbr;

	@JsonProperty("FinancialServices.ColorName")
	public String ColorName;

}
