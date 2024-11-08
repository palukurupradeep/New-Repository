package com.shaw.claims.services;

import com.shaw.claims.dto.SampleRequestDTO;
import com.shaw.claims.dto.SampleReviewDTO;
import com.shaw.claims.model.ClaimReasonDefinition;

public interface SampleReviewService {
	public void sampleRequest(SampleRequestDTO sampleRequestDTO);

	public ClaimReasonDefinition fetchSampleSizeRequirementAndTesting(String reasonCode);

	public void updateSampleReviewByAction(SampleReviewDTO sampleReviewDTO);

	
}
