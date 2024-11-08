package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimRateDetailResponseDTO {
	private ClaimRateDTO customerClaimRateDetailDTO;
	private ClaimRateDTO shawClaimRateDetailDTO;
	private ClaimRateDTO cumulativeClaimRateDetailDTO;
	private ClaimRateDTO buyingGroupClaimRateDetailDTO;
}
