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
public class GetInvoiceChargeResponse {
	public String invoiceNbr;
	public String invoiceDate;
	public String ChargeCode;
	public String description;
	public double totalCharge;
	public String seqNbr;
	public double unitPrice;
	public double quantity;
}
