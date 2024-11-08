package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.ClaimCodeResolutionDTO;
import com.shaw.claims.dto.ClaimResolutionDTO;
import com.shaw.claims.dto.CodeResolutionCreateDTO;
import com.shaw.claims.model.ClaimCodeResolution;
import com.shaw.claims.model.ClaimResolution;

public interface ClaimCodeResolutionService {

	List<ClaimResolution> getAllClaimResolutions();

	 ClaimCodeResolution createCodeResolution(CodeResolutionCreateDTO request);

	void deleteCodeResolution(int codeResolutionId);

	ClaimResolution addOrUpdateClaimResolution(ClaimResolutionDTO request);

	void deleteClaimResolution(int claimResolutionId);

	List<ClaimCodeResolutionDTO> getClaimCodeResolution(Integer reasonId);
}
