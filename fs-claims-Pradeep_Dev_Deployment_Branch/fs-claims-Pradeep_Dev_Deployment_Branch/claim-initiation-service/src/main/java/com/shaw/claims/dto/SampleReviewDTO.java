package com.shaw.claims.dto;

import lombok.Data;

@Data
public class SampleReviewDTO extends BaseEntityDTO{
	 private Integer claimId;
	 private String action;
	 private String noteTemplateText;
}
