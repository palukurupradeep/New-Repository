package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class PriorClaimSummaryResponseDTO {

	private String status;

	private String claimNumber;

	private LocalDateTime claimDate;

	private String customerNumber;

	private String claimCategory;

	private String name;

	private List<ClaimDocumentResponseDTO> invoiceData;

	private String reasonCode;
	
	private String reasonDescription;

	private BigDecimal totalAmount;
	
	private List<ClaimCrmNumberResponseDTO> crmNumberList;
	
	private String origin;
	
	private String dbt;
	
	private LocalDateTime date1;
}
