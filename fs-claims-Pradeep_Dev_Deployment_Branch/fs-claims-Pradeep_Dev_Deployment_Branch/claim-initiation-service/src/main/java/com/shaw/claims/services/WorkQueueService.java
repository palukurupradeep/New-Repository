package com.shaw.claims.services;

import java.util.List;
import java.util.Map;

import com.shaw.claims.dto.UserWorkQueueFieldDTO;
import com.shaw.claims.dto.WorkQueueFieldsResponseDTO;
import com.shaw.claims.dto.WorkQueueResponseDTO;

public interface WorkQueueService {
	WorkQueueResponseDTO getClaimWorkQueues(int userId);
	void addUserWorkQueueFields(UserWorkQueueFieldDTO userWorkQueueFieldDTO);
	void deleteUserWorkQueueFields(Integer createdByUserId);
	List<WorkQueueFieldsResponseDTO> fetchUserWorkQueueFieldsByUserId(Integer createdByUserId);
	WorkQueueResponseDTO getPriorityClaims(Integer userId);
	WorkQueueResponseDTO lastActivityClaims(Integer userId,Integer days);
	WorkQueueResponseDTO agedClaims(Integer userId,Integer days);
	Map<String, String> previousDayClaims(Integer userId);
	List<WorkQueueFieldsResponseDTO> fetchWorkQueueFields();
}
