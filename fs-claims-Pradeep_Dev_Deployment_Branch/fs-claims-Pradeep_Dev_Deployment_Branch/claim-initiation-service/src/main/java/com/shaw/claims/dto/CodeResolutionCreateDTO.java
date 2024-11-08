package com.shaw.claims.dto;

import lombok.Data;

@Data
public class CodeResolutionCreateDTO extends BaseEntityDTO {
	
	private Integer claimReasonId;
	
	private Integer resolutionId;
	
}
