package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AssociateDocumensDto {
	  private int claimDocumentId;
	  private String documentNumber;
	  private String documentTypeCode;
	  private String status;
	  private int claimId;
	  private LocalDateTime documentDate;
	  private BigDecimal amountUsd;
	  private int headerId;
	  private String orderNumber = "";
	  private String purchaseOrderNumber = "";
	  private String billofLading = "";
	  private String masterBillofLading = "";
	 
}
