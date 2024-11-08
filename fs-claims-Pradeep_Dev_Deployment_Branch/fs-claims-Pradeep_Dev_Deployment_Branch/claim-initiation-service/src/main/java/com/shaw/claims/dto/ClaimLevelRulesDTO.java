package com.shaw.claims.dto;

import java.util.List;

import lombok.Data;

@Data
public class ClaimLevelRulesDTO {

	private String reasonCode;
	private Integer claimId;
	private List<LineDetailsRequest> lineDetails;
	private String ruleOfImpactArea;
	private Boolean addFreightLine;
	private String customerNumber;
	private Boolean updateReasonCodeLineItem;
}
