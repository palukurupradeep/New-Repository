package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FINServicesGetInvoiceChargesResult {
	@JsonProperty("FinancialServices.InvoiceNbr")
	public String InvoiceNbr;
	@JsonProperty("FinancialServices.InvoiceDate")
	public String InvoiceDate;
	@JsonProperty("FinancialServices.ChargeCode")
	public String ChargeCode;
	@JsonProperty("FinancialServices.Description")
	public String Description;
	@JsonProperty("FinancialServices.TotalCharge")
	public double TotalCharge;
	@JsonProperty("FinancialServices.SeqNbr")
	public String SeqNbr;
	@JsonProperty("FinancialServices.UnitPrice")
	public double UnitPrice;
	@JsonProperty("FinancialServices.Quantity")
	public double Quantity;
}
