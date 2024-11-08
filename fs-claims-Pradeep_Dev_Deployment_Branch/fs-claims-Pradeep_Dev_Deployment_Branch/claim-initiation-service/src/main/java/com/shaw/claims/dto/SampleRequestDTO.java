package com.shaw.claims.dto;

import lombok.Data;

@Data
public class SampleRequestDTO extends BaseEntityDTO{

	 private int claimId;
	 private String dealerEmail;
	 private String endUserEmail;
	 private String sampleRequirement;
	 private Boolean sendEmail;
	 private Boolean generateFedExLable;
}
