package com.shaw.claims.services;

import com.shaw.claims.dto.AuditHistoryDTO;

import java.util.List;

public interface AuditHistoryService {
	public List<AuditHistoryDTO> fetchAuditHistoryByClaimId(Integer claimId);
}
