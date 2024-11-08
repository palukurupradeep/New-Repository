package com.shaw.claims.services;

import java.io.IOException;
import java.util.List;

import com.shaw.claims.dto.SearchClaimHistoryRequestDTO;
import com.shaw.claims.dto.SearchClaimHistoryResponseDTO;
import com.shaw.claims.dto.SecondarySearchDTO;
import com.shaw.claims.model.ClaimStatus;
import com.shaw.claims.model.Lookup;

public interface SearchClaimHistoryService {

	
	
	List<SearchClaimHistoryResponseDTO> searchClaimHistory(SearchClaimHistoryRequestDTO searchClaimHistoryRequest);

	List<ClaimStatus> getClaimStatus();
	List<Lookup> getPrimarySearch();
	List<SecondarySearchDTO> getSecondarySearch(String primarySearchType);
//	 List<String> getSecondarySearch(String primarySearchType);
	 byte[] exportToExcel(SearchClaimHistoryRequestDTO searchClaimHistoryRequest) throws IOException;
}
