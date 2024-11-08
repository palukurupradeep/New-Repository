package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesGetCustomerSalesCustomerServiceDataResult {
	@JsonProperty("FinancialServices.CustomerNumber")
	public String CustomerNumber;
	@JsonProperty("FinancialServices.CustomerName")
	public String CustomerName;
	@JsonProperty("FinancialServices.CustomerType")
	public String CustomerType;
	@JsonProperty("FinancialServices.CustomerReferenceNumber")
	public String CustomerReferenceNumber;
	@JsonProperty("FinancialServices.CustomerReferenceNumberDesc")
	public String CustomerReferenceNumberDesc;
	@JsonProperty("FinancialServices.StoreType")
	public String StoreType;
	@JsonProperty("FinancialServices.StoreTypeDesc")
	public String StoreTypeDesc;
	@JsonProperty("FinancialServices.StoreTypeGroupCode")
	public String StoreTypeGroupCode;
	@JsonProperty("FinancialServices.GroupAccount")
	public String GroupAccount;
	@JsonProperty("FinancialServices.CCAAccount")
	public String CCAAccount;
	@JsonProperty("FinancialServices.AlignedAccount")
	public String AlignedAccount;
	@JsonProperty("FinancialServices.InsideSalesGroup")
	public String InsideSalesGroup;
	@JsonProperty("FinancialServices.HomeCenter")
	public String HomeCenter;
	@JsonProperty("FinancialServices.SFNActiveDate")
	public Object SFNActiveDate;
	@JsonProperty("FinancialServices.SFNDeactivateDate")
	public Object SFNDeactivateDate;
	@JsonProperty("FinancialServices.PrimarySellingCompany")
	public String PrimarySellingCompany;
	@JsonProperty("FinancialServices.PrimaryTerritory")
	public String PrimaryTerritory;
	@JsonProperty("FinancialServices.Comments")
	public String Comments;
	@JsonProperty("FinancialServices.ShipCode")
	public String ShipCode;
	@JsonProperty("FinancialServices.ShipCodeName")
	public String ShipCodeName;
	@JsonProperty("FinancialServices.ShipAuthorizationRequired")
	public String ShipAuthorizationRequired;
	@JsonProperty("FinancialServices.CustomsBroker")
	public String CustomsBroker;
	@JsonProperty("FinancialServices.CustomsBrokerName")
	public String CustomsBrokerName;
	@JsonProperty("FinancialServices.ESPCustomer")
	public String ESPCustomer;
}
