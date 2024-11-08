package com.shaw.claims.source.request;

import lombok.Data;

@Data
public class InvoiceDetailsRequest {
	private String customerNumber;
	private String invoiceNumber;
	private String bolNumber;
	private String poNumber;
	private String orderNumber;
	private String invoiceDate;
	private String docType;
	private String rollNumber;
}
