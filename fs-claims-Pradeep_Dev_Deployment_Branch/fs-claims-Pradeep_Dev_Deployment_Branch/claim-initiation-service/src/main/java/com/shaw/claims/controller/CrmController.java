package com.shaw.claims.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.AllowanceToKeepDTO;
import com.shaw.claims.dto.AssociateDocumensDto;
import com.shaw.claims.dto.ClaimCrmHeaderDTO;
import com.shaw.claims.dto.CrmIssueResponseDTO;
import com.shaw.claims.dto.DeclinCreditMemoResponseDTO;
import com.shaw.claims.dto.DispositionTypeDTO;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.services.CrmService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/crm/v1")
public class CrmController {
	
	Logger log = LogManager.getLogger(CrmController.class);
	
	@Autowired
	CrmService crmService;
	
	@PostMapping("/crmHoldOrIssue")
	public ResponseEntity<CrmIssueResponseDTO>  crmHoldOrIssue(@RequestBody ClaimCrmHeaderDTO claimCrmHeaderDTO) {
		log.info("inside.CrmController.crmHoldOrIssue");
		return new ResponseEntity<>(crmService.crmHoldOrIssue(claimCrmHeaderDTO),HttpStatus.OK);
	}
	
	@GetMapping("/fetchCrmDetailsByClaimIdAndCrmNumber")
	public ResponseEntity<ClaimCrmHeaderDTO> fetchCrmDetailsByClaimIdAndCrmNumber(@RequestParam int claimId,String crmNumber) {
		log.info("inside.CrmController.fetchCrmDetailsByClaimIdAndCrmNumber");
		 return new ResponseEntity<>(crmService.fetchCrmDetailsByClaimIdAndCrmNumber(claimId,crmNumber),HttpStatus.OK);
		 
	}
	
	@GetMapping("/fetchDocumentsByClaimId")
	public ResponseEntity<List<AssociateDocumensDto>> fetchDocumentsByClaimId(@RequestParam int claimId) {
		log.info("inside.CrmController.fetchDocumentsByClaimId");
		 return new ResponseEntity<>(crmService.fetchDocumentsByClaimId(claimId),HttpStatus.OK);
		 
	}
	
	@PutMapping("/declineCreditMemo")
	public ResponseEntity<DeclinCreditMemoResponseDTO> declineCreditMemo(@RequestParam int claimCrmHeaderId, @RequestParam int userId,@RequestParam String noteText){
		log.info("inside.CrmController.declineCreditMemo");
		return new ResponseEntity<>(crmService.declineCreditMemo(claimCrmHeaderId, userId,noteText),HttpStatus.OK);
	}
	
	@PutMapping("/updateCrmDetailsByAction")
	@Operation(description = "<h2><b>Possible actions are : Void,Cancelled.</h2></b>")
	public void updateCrmDetailsByAction(@RequestParam int claimCrmHeaderId,@RequestParam String Action, @RequestParam int userId,@RequestParam String noteText) {
		log.info("inside.CrmController.updateCrmDetailsByAction");
		crmService.updateCrmDetailsByAction(claimCrmHeaderId,Action,userId,noteText);
	}

	@PostMapping("/creditMemoValidation")
	public void  creditMemoValidation(@RequestParam int claimCrmHeaderId, @RequestParam int userId, @RequestParam String customerNumber, @RequestParam Boolean isTraceTaskOpen){
		log.info("inside.CrmController.creditMemoValidation");
		 crmService.creditMemoValidation(claimCrmHeaderId, userId, customerNumber, isTraceTaskOpen);
	}
	
	@GetMapping("/getAllDispositionTypes")
	public ResponseEntity<List<DispositionTypeDTO>>  getAllDispositionTypes(){
		log.info("inside.CrmController.getAllDispositionTypes");
		 return new ResponseEntity<List<DispositionTypeDTO>>( crmService.getAllDispositionTypes() ,HttpStatus.OK);
	}
	
	@PostMapping("/getAllowanceToKeepData")
	public ResponseEntity<List<ClaimReasonDefinition>>  getAllowanceToKeepData(@RequestBody AllowanceToKeepDTO allowanceToKeepDTO){
		log.info("inside.CrmController.getAllDispositionTypes");
		 return new ResponseEntity<List<ClaimReasonDefinition>>( crmService.getAllowanceToKeepData(allowanceToKeepDTO) ,HttpStatus.OK);
	}
	
	
}
