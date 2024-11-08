package com.shaw.claims.source.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SearchClaimInvoiceDetailsRequest extends InvoiceDetailsRequest {
	public String fromDate;

	public String toDate;

	private String commonCustomerNumber;

	private String styleNum;

	private String colorNum;

	private int proNum;

	private String mastBolNum;

}
