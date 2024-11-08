package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SearchClaimInvoiceDetails {
	public String customerNumber;
	public String billtoName;
	public String invoiceNbr;
	public String invoiceDate;
	public String bolNumber;
	public String orderNumber;
	public String orderDate;
	public String invType;
	public String sellco;
	public double totalTaxAmt;
	public double totalInvoiceAmt;
	public String shipToState;
	private String pONbr;
}
