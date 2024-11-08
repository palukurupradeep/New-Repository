package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ClaimAssignmentData {
	public String customerNumber;
	public String customerName;
	public String claimsArea;
	public String claimsAreaName;
	public String businessSegment;
	public String businessSegmentName;
	public String homeCenter;
	public String alignedAccount;
	public String customerReferenceNumber;
	public String customerReferenceNumberDesc;
	public String ccaAccount;
}