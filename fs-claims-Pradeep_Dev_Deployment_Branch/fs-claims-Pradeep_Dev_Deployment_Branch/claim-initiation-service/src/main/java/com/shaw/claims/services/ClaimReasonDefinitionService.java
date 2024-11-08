package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.AddOrUpdateReasonDefinitionDTO;
import com.shaw.claims.model.ClaimReasonDefinition;

public interface ClaimReasonDefinitionService {
    public List<ClaimReasonDefinition> getReasonCodeByCategory(Integer categoryId);

	public AddOrUpdateReasonDefinitionDTO addReasonDefinition(AddOrUpdateReasonDefinitionDTO request);

	public AddOrUpdateReasonDefinitionDTO updateReasonDefinition(AddOrUpdateReasonDefinitionDTO request);
}
