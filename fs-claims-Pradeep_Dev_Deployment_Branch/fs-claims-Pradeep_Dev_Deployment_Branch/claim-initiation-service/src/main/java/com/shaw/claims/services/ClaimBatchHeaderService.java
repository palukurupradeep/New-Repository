package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.ClaimBatchDetailsDTO;
import com.shaw.claims.dto.ClaimBatchHeaderDTO;
import com.shaw.claims.model.BatchStatus;

public interface ClaimBatchHeaderService {

	void create(List<ClaimBatchHeaderDTO> claimBatchHeaderDTOS);

	List<BatchStatus> getBatchStatus();

	List<ClaimBatchDetailsDTO> getClaimBatchDetail(Integer claimBatchHeaderId);

	List<ClaimBatchHeaderDTO> fetchClaimBatchHeader(ClaimBatchHeaderDTO claimBatchHeaderDTO);

}
