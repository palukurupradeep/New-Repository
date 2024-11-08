package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.WorkHistoryDTO;
import com.shaw.claims.model.Claim;

public interface WorkHistoryService {
	
	void updateWorkHistory(Claim claim, Integer modifiedUserId);

	List<WorkHistoryDTO> getWorkHistory(Integer userId);

    void setWorkHistory(Claim claim);
}
