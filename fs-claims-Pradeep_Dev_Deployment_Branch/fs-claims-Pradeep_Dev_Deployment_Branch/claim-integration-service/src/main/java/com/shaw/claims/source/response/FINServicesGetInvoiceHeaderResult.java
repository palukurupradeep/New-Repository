package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FINServicesGetInvoiceHeaderResult {
	@JsonProperty("FinancialServices.CustomerNumber")
	public String CustomerNumber;
	@JsonProperty("FinancialServices.InvoiceNbr")
	public String InvoiceNbr;
	@JsonProperty("FinancialServices.InvoiceDate")
	public String InvoiceDate;
	@JsonProperty("FinancialServices.TotalFreightAmt")
	public double TotalFreightAmt;
	@JsonProperty("FinancialServices.TotalTaxAmt")
	public double TotalTaxAmt;
	@JsonProperty("FinancialServices.TotalInvoiceAmt")
	public double TotalInvoiceAmt;
	@JsonProperty("FinancialServices.PricingCurrencyCode")
	public String PricingCurrencyCode;
	@JsonProperty("FinancialServices.CurrencyCodeDesignation")
	public String CurrencyCodeDesignation;
	@JsonProperty("FinancialServices.CurrencyCodeName")
	public String CurrencyCodeName;
	@JsonProperty("FinancialServices.ExchangeRate")
	public double ExchangeRate;
	@JsonProperty("FinancialServices.BilltoName")
	public String BilltoName;
	@JsonProperty("FinancialServices.BilltoAddr1")
	public String BilltoAddr1;
	@JsonProperty("FinancialServices.BilltoAddr2")
	public String BilltoAddr2;
	@JsonProperty("FinancialServices.BilltoCity")
	public String BilltoCity;
	@JsonProperty("FinancialServices.BilltoState")
	public String BilltoState;
	@JsonProperty("FinancialServices.BilltoZip")
	public String BilltoZip;
	@JsonProperty("FinancialServices.BilltoCounty")
	public String BilltoCounty;
	@JsonProperty("FinancialServices.ShiptoName")
	public String ShiptoName;
	@JsonProperty("FinancialServices.ShiptoAddr1")
	public String ShiptoAddr1;
	@JsonProperty("FinancialServices.ShiptoAddr2")
	public String ShiptoAddr2;
	@JsonProperty("FinancialServices.ShiptoCity")
	public String ShiptoCity;
	@JsonProperty("FinancialServices.ShiptoState")
	public String ShiptoState;
	@JsonProperty("FinancialServices.ShiptoZip")
	public String ShiptoZip;
	@JsonProperty("FinancialServices.ShipVia")
	public String ShipVia;
	@JsonProperty("FinancialServices.RDCLoc")
	public String RDCLoc;
	@JsonProperty("FinancialServices.ShawInd")
	public String ShawInd;
	@JsonProperty("FinancialServices.TaxPercent")
	public double TaxPercent;
}
