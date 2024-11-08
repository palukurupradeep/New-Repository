package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimResolutionDTO extends BaseEntityDTO {
	
	private Integer resolutionId;
	
	private String resolutionName;

	private String description;

	private String procedureManual;
	
}
