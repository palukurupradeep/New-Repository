package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.*;
import com.shaw.claims.model.*;

public interface ClaimDetailsService {
	ClaimInitiationResponseDTO jobStopToggle(int claimId, boolean jobStopped, int userId);
	ClaimInitiationResponseDTO priorityClaimToggle(int claimId, boolean priorityClaim, String noteText, int userId);
	List<ClaimReasonDefinition> getAllReasonCodes();
	ClaimReasonDefinition findByClaimReasonCode(String reasonCode);
	Claim getClaimByClaimNumber(String claimNumber, int userId);
	ClaimWatchlist getClaimWatchlist(int claimId, int userId);
	ClaimWatchlistDTO claimWatchlistToggle(ClaimWatchlistDTO claimWatchlistDTO);
	List<SellingCompany> getAllSellingCompanyData();
	EndUserInformationDTO addorUpdateEndUserInfo(EndUserInfoUpdateDTO endUserInfoUpdateDTO);
	void updateCustomerDetails(Integer claimId, String customerNumber);
	UpdateClaimStatusResponseDTO updateClaimStatusByAction(Integer claimId, String action, Integer declineReasonId,Integer userId,String territoryManagerName,String declineReasonText);
	List<DeclineReason> getAllDeclinedReasons();
	void addInvoice(ClaimDTO claimDTO);
	void updateLineItem(List<ClaimDocumentDTO> claimDocumentDTO);
	void deleteLineItem(List<Integer> claimLineIds);
	void duplicateLineItem(ClaimDocumentDTO claimDocumentDTO);
	void addMoreLineItems(ClaimDocumentDTO claimDocumentDTO);
	ClaimRateDetailResponseDTO getClaimRateDetails(String customerNumber);
	List<PriorClaimSummaryResponseDTO> getPriorClaimForInvoice(String documentNumber, String documentTypeId, String claimId);
	void reAssignUser(int claimId, int adminId,int userId);
	List<Claim> getClaims(Integer userId, String customerNumber);
	List<Claim> getClaims(List<Integer> userIds);
	List<DocumentType> getAllActiveDocuments();
	List<DetailType> getAllLineCategory();
	String findDetailTypeCodeByDetailTypeId(String detailTypeId);
	List<LineAddReason> getAllClaimLineAddReason();
	void laborLineItem(ClaimDocumentDTO claimDocumentDTO);
	void serviceLineItem(ClaimDocumentDTO claimDocumentDTO);
	List<UnitOfMeasure> getClaimUnitOfMeasure();

	PrepareRGAResponseDTO prepareRGA(RGARequestDTO rgaRequestDTO);
	void routed(ClaimRoutedDTO claimRoutedDTO);
	List<ClaimRoute> routedClaim(ClaimRoutedDTO claimRoutedDTO, Claim claim, Boolean isInStoreClaim);
	void updateSellingCompanyAndReasoCode(UpdateReasonCodeDTO updateReasonCodeDTO);
	void addGenericDocument(ClaimDocumentDTO claimDocumentDTO);
	UpdateClaimStatusResponseDTO updatePauseClaimNote(UpdatePauseClaimNoteRequestDTO updatePauseClaimNoteRequestDTO);
}
