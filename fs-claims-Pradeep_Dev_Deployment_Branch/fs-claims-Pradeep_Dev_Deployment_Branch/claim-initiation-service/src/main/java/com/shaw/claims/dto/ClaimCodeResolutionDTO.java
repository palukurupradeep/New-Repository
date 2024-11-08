package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimCodeResolutionDTO extends BaseEntityDTO {

	private Integer codeResolutionId;

	private Integer reasonDefinitonId;

	private String reasonCode;

	private String reasonDescription;

	private ClaimResolutionDTO claimResolution;

	private Integer status;

}
