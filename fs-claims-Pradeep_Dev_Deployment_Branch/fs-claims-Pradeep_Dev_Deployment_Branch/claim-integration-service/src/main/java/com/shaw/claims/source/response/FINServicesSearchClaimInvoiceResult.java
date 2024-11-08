package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FINServicesSearchClaimInvoiceResult {
	@JsonProperty("FinancialServices.CustomerNumber")
	public String CustomerNumber;
	@JsonProperty("FinancialServices.BilltoName")
	public String BilltoName;
	@JsonProperty("FinancialServices.InvoiceNbr")
	public String InvoiceNbr;
	@JsonProperty("FinancialServices.InvoiceDate")
	public String InvoiceDate;
	@JsonProperty("FinancialServices.BolNumber")
	public String BolNumber;
	@JsonProperty("FinancialServices.OrderNumber")
	public String OrderNumber;
	@JsonProperty("FinancialServices.OrderDate")
	public String OrderDate;
	@JsonProperty("FinancialServices.InvType")
	public String InvType;
	@JsonProperty("FinancialServices.Sellco")
	public String Sellco;
	@JsonProperty("FinancialServices.TotalTaxAmt")
	public double TotalTaxAmt;
	@JsonProperty("FinancialServices.TotalInvoiceAmt")
	public double TotalInvoiceAmt;
	@JsonProperty("FinancialServices.ShipToState")
	public String ShipToState;
	@JsonProperty("FinancialServices.PONbr")
	public String PONbr;
}
