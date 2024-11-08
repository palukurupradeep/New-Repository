package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.AllowanceToKeepDTO;
import com.shaw.claims.dto.AssociateDocumensDto;
import com.shaw.claims.dto.ClaimCrmHeaderDTO;
import com.shaw.claims.dto.CrmIssueResponseDTO;
import com.shaw.claims.dto.DeclinCreditMemoResponseDTO;
import com.shaw.claims.dto.DispositionTypeDTO;
import com.shaw.claims.model.ClaimReasonDefinition;

public interface CrmService {

	public CrmIssueResponseDTO crmHoldOrIssue(ClaimCrmHeaderDTO claimCrmHeaderDTO);

	public ClaimCrmHeaderDTO fetchCrmDetailsByClaimIdAndCrmNumber(int claimId, String crmNumber);

	public List<AssociateDocumensDto> fetchDocumentsByClaimId(int claimId);

	public DeclinCreditMemoResponseDTO declineCreditMemo(int claimCrmHeaderId, int userId,String noteText);

	public void creditMemoValidation(int claimCrmHeaderId, int userId, String customerNumber, Boolean isTraceTaskOpen);
	public void updateCrmDetailsByAction(int claimCrmHeaderId, String action, int userId,String noteText);

	public List<DispositionTypeDTO> getAllDispositionTypes();

	public List<ClaimReasonDefinition> getAllowanceToKeepData(AllowanceToKeepDTO allowanceToKeepDTO);


}
