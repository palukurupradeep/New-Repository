package com.shaw.claims.dto;

import lombok.Data;

@Data
public class CodePhotosDTO extends BaseEntityDTO {

	ClaimReasonDefinitionDTO claimReasonDefinitionDTO;
	ClaimPhotosDTO claimPhotosDTO;
}
