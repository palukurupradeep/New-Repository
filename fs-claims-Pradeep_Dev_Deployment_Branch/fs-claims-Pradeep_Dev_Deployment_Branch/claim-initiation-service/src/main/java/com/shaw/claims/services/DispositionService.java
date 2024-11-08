package com.shaw.claims.services;

import com.shaw.claims.dto.ClaimDispositionHeaderDTO;

public interface DispositionService {
	public void dispositionHoldOrIssue(ClaimDispositionHeaderDTO claimDispositionHeaderDTO);

	public void DispositonVoid(int claimDispositionHeaderId);

	public ClaimDispositionHeaderDTO fetchDispositonDetailsByClaimIdAndDispositonNumber(int claimId, String dispositionNumber);
}
