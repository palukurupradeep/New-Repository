package com.shaw.claims.services;

import com.shaw.claims.dto.*;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.*;

import java.util.List;
import java.util.Set;


public interface ClaimService {
	ClaimInitiationResponseDTO initiate(ClaimDTO claimDTO);
	List<State> getStatesByCountryId(Integer countryId);
	List<Country> getCountries();
	List<DialCode> getDialCodes();

	ResponseDTO claimSubmittal(ClaimSubmittalDTO claimSubmittalDTO);
	List<PriorClaimSummaryResponseDTO> getPriorClaimSummary(PClaimDTO claimDto) throws CommonException;
	void claimAssignment(Integer claimId, Integer claimUserId);
	void automaticAssignment(List<Integer> claimId);
	ClaimStatus getClaimStatusById(Integer claimStatusId);
	WorkStatusDTO getWorkStatusById(Integer workStatusId, Integer userId);
	Set<ContactDetailsDTO> getContactDetails(String customerNumber);
	ClaimUpdateAddressDTO updateClaimAddress(ClaimUpdateAddressDTO claimUpdateAddressDTO);
	void merge(MergedAndUnMergedRequestDTO mergedRequestDTO);
	void unMerge(MergedAndUnMergedRequestDTO unMergedRequestDTO);
	void deleteDocument(int claimDocumentId, int loggedInUserId);
	List<MergedUnMergedResponseDTO> unMergedClaims(int parentClaimId, String customerNumber);
	List<MergedUnMergedResponseDTO> mergedClaims(int parentClaimId);
	ClaimDocument claimAssociateDocument(AssociateDocumentDTO associateDocumentDTO);
	List<WorkStatus> getWorkStatus();
	void claimRoute(ClaimRouteRequestDTO claimRouteRequestDTO);
	Object getClaim(String claimNumber, String type);
	ClaimInitiationResponseDTO claimInitiate(ClaimDTO claimDTO);
}
