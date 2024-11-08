package com.shaw.claims.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.AuditHistoryDTO;
import com.shaw.claims.dto.ClaimDTO;
import com.shaw.claims.dto.ClaimDocumentDTO;
import com.shaw.claims.dto.ClaimInitiationResponseDTO;
import com.shaw.claims.dto.ClaimLevelRulesDTO;
import com.shaw.claims.dto.ClaimRateDetailResponseDTO;
import com.shaw.claims.dto.ClaimRoutedDTO;
import com.shaw.claims.dto.ClaimWatchlistDTO;
import com.shaw.claims.dto.EndUserInfoUpdateDTO;
import com.shaw.claims.dto.EndUserInformationDTO;
import com.shaw.claims.dto.PrepareRGAResponseDTO;
import com.shaw.claims.dto.PriorClaimSummaryResponseDTO;
import com.shaw.claims.dto.RGARequestDTO;
import com.shaw.claims.dto.SmartCodeMessageDTO;
import com.shaw.claims.dto.UpdateClaimStatusResponseDTO;
import com.shaw.claims.dto.UpdatePauseClaimNoteRequestDTO;
import com.shaw.claims.dto.UpdateReasonCodeDTO;
import com.shaw.claims.model.Claim;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.model.ClaimWatchlist;
import com.shaw.claims.model.DeclineReason;
import com.shaw.claims.model.DetailType;
import com.shaw.claims.model.DocumentType;
import com.shaw.claims.model.LineAddReason;
import com.shaw.claims.model.SellingCompany;
import com.shaw.claims.model.UnitOfMeasure;
import com.shaw.claims.services.AuditHistoryService;
import com.shaw.claims.services.ClaimDetailsService;
import com.shaw.claims.services.SmartCodeRulesService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/claimdetails/v1")
public class ClaimDetailsController {
	Logger log = LogManager.getLogger(ClaimDetailsController.class);
	@Autowired
	ClaimDetailsService claimDetailsService;
	@Autowired
	SmartCodeRulesService smartCodeRulesService;
    @Autowired
    AuditHistoryService auditHistoryService;
	
	@PutMapping("/jobStopToggle")
    public ResponseEntity<ClaimInitiationResponseDTO> jobStopToggle(@RequestParam(name = "claimId") int claimId, @RequestParam(name = "jobStopped") boolean jobStopped,@RequestParam(name = "userId") int userId){
        log.info("Inside ClaimDetailsController.JobStopToggle");
        return new ResponseEntity<>(claimDetailsService.jobStopToggle(claimId,jobStopped,userId), HttpStatus.OK);
    }
	
	@PutMapping("/priorityClaimToggle")
    public ResponseEntity<ClaimInitiationResponseDTO> priorityClaimToggle(@RequestParam(name = "claimId") int claimId,
                                                                          @RequestParam(name = "priorityClaim") boolean priorityClaim,
                                                                          @RequestParam(name = "noteText", required = false) String noteText,
                                                                          @RequestParam(name = "userId") int userId){
        log.info("Inside ClaimDetailsController.priorityClaimToggle");
        return new ResponseEntity<>(claimDetailsService.priorityClaimToggle(claimId,priorityClaim,noteText,userId), HttpStatus.OK);
    }

    @GetMapping("/getAllReasonCodes")
    public ResponseEntity<List<ClaimReasonDefinition>> getAllReasonCodes(){
        log.info("Inside ClaimDetailsController.getAllReasonCodes");
        return new ResponseEntity<>(claimDetailsService.getAllReasonCodes(),HttpStatus.OK);
    }
    @GetMapping("/findByClaimReasonCode")
    public ResponseEntity<ClaimReasonDefinition> findByClaimReasonCode(@RequestParam(name = "reasonCode") String reasonCode){
        log.info("Inside ClaimDetailsController.findByClaimReasonCode");
        return new ResponseEntity<>(claimDetailsService.findByClaimReasonCode(reasonCode),HttpStatus.OK);
    }

    @GetMapping("/getClaim")
    public ResponseEntity<Claim> getClaimByClaimNumber(@RequestParam(name = "claimNumber") String claimNumber, @RequestParam(name = "userId") int userId){
        log.info("Inside ClaimDetailsController.getClaimByClaimNumber");
        return new ResponseEntity<>(claimDetailsService.getClaimByClaimNumber(claimNumber, userId),HttpStatus.OK);
    }

    @GetMapping("/hasClaimWatchlist")
    public ResponseEntity<Boolean> hasClaimWatchlist(@RequestParam(name = "claimId") int claimId,
            @RequestParam(name = "userId") int userId){
        log.info("Inside ClaimDetailsController.hasClaimWatchlist");
        Boolean hasWatchlist = false;
        ClaimWatchlist claimWatchlist = claimDetailsService.getClaimWatchlist(claimId, userId);
        if(claimWatchlist!=null)
            hasWatchlist = true;
        return new ResponseEntity<Boolean>(hasWatchlist,HttpStatus.OK);
    }
    
    @PutMapping("/claimWatchlistToggle")
    public ResponseEntity<ClaimWatchlistDTO> claimWatchlistToggle(@RequestBody ClaimWatchlistDTO claimWatchlistDTO){
        log.info("Inside ClaimDetailsController.claimWatchlistToggle");
        return new ResponseEntity<>(claimDetailsService.claimWatchlistToggle(claimWatchlistDTO), HttpStatus.OK);
    }
    
    @GetMapping("/getAllSellingCompanyData")
    public ResponseEntity<List<SellingCompany>> getAllSellingCompanyData(){
        log.info("Inside UserController.getAllSellingCompanyData");
        return new ResponseEntity<>(claimDetailsService.getAllSellingCompanyData(),HttpStatus.OK);
    }
    
    @PostMapping("/addorUpdateEndUserInfo")
    public ResponseEntity<EndUserInformationDTO> addorUpdateEndUserInfo(@RequestBody EndUserInfoUpdateDTO endUserInfoUpdateDTO)
    {
        log.info("Inside UserController.addorUpdateEndUserInfo");
        return new ResponseEntity<EndUserInformationDTO>(claimDetailsService.addorUpdateEndUserInfo(endUserInfoUpdateDTO),HttpStatus.OK);
    }
    @PutMapping("/updateCustomerDetails")
    public ResponseEntity<Void> updateCustomerDetails(@RequestParam(name = "claimId", required = false) Integer claimId, @RequestParam(name = "customerNumber", required = false) String customerNumber) {
        log.info("Inside ClaimsController.updateCustomerDetails requestParam :: {}", claimId, customerNumber);
        claimDetailsService.updateCustomerDetails(claimId,customerNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/updateClaimStatusByAction")
    @Operation(description = "<h2><b>Possible actions are : Pause, Decline, Void, Reopen, Close, Request-Sample, AssignedToPricing, RemovedFromPricing, RequestForInformation. </h2></b>")
    public ResponseEntity<UpdateClaimStatusResponseDTO> updateClaimStatusByAction(@RequestParam(name = "claimId", required = false) Integer claimId, @RequestParam(name = "action", required = false) String action,  @RequestParam(name = "declineReasonId", required = false) Integer declineReasonId,
    		@RequestParam(name = "userId", required = false) Integer userId,@RequestParam(name = "territoryManagerName", required = false) String territoryManagerName,@RequestParam(name = "noteText", required = false) String noteText) {
        log.info("Inside ClaimsController.updateClaimStatusByAction requestParam :: {}, {}", claimId, action);
        UpdateClaimStatusResponseDTO response = claimDetailsService.updateClaimStatusByAction(claimId,action, declineReasonId,userId,territoryManagerName,noteText);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
   @GetMapping("/getAllDeclinedReasons")
    public ResponseEntity<List<DeclineReason>> getAllDeclinedReasons(){
        log.info("Inside UserController.getAllDeclinedReasons");
        return new ResponseEntity<>(claimDetailsService.getAllDeclinedReasons(),HttpStatus.OK);
    }
    @PostMapping("/addInvoice")
    public ResponseEntity<Void> addInvoice(@RequestBody ClaimDTO claimDTO){
        claimDetailsService.addInvoice(claimDTO);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("/updateLineItem")
    public ResponseEntity<Void> updateLineItem(@RequestBody List<ClaimDocumentDTO> claimDocumentDTO){
        log.info("Inside ClaimDetailsController.updateLineItem");
        claimDetailsService.updateLineItem(claimDocumentDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping("/deleteLineItem")
    public ResponseEntity<Void> deleteLineItem(@RequestParam(name="claimLineIds") List<Integer> claimLineIds){
        log.info("Inside ClaimDetailsController.deleteLineItem");
        claimDetailsService.deleteLineItem(claimLineIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping("/duplicateLineItem")
    public ResponseEntity<Void> duplicateLineItem(@RequestBody ClaimDocumentDTO claimDocumentDTO){
        log.info("Inside ClaimDetailsController.duplicateLineItem");
        claimDetailsService.duplicateLineItem(claimDocumentDTO);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @PostMapping("/addMoreLineItems")
    public ResponseEntity<Void> addMoreLineItems(@RequestBody ClaimDocumentDTO claimDocumentDTO){
        claimDetailsService.addMoreLineItems(claimDocumentDTO);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/getClaimRateDetails")
    public ResponseEntity<ClaimRateDetailResponseDTO> getClaimRateDetails(@RequestParam(name = "customerNumber") String customerNumber){
        log.info("Inside ClaimDetailsController.getClaimRateDetails");
        return new ResponseEntity<>(claimDetailsService.getClaimRateDetails(customerNumber),HttpStatus.OK);
    }
    @GetMapping("/getPriorClaimForInvoice")
    public ResponseEntity<List<PriorClaimSummaryResponseDTO>> getPriorClaimForInvoice(@RequestParam(name = "documentNumber") String documentNumber,
                                                               @RequestParam(name = "documentTypeId") String documentTypeId,
                                                               @RequestParam(name = "claimId") String claimId){
        log.info("Inside ClaimDetailsController.getPriorClaimForInvoice");
        return new ResponseEntity<>(claimDetailsService.getPriorClaimForInvoice(documentNumber, documentTypeId, claimId),HttpStatus.OK);
    }

    @PutMapping("/reAssign")
    public ResponseEntity<Void> reAssignUser(@RequestParam(name = "claimId") int claimId, @RequestParam(name = "adminId") int adminId,@RequestParam(name = "userId") int userId){
        log.info("Inside UserController.reAssignUser");
        claimDetailsService.reAssignUser(claimId, adminId,userId);
     return  new ResponseEntity<>(HttpStatus.OK);
    }
    
    @GetMapping("/getClaims")
    public ResponseEntity<List<Claim>> getClaims(@RequestParam(name = "userId", required = false) Integer userId,
                                                        @RequestParam(name = "customerNumber", required = false) String customerNumber){
        log.info("Inside ClaimDetailsController.getClaims");
        return new ResponseEntity<>(claimDetailsService.getClaims(userId, customerNumber),HttpStatus.OK);
    }
    
    @GetMapping("/getAllActiveDocuments")
    public ResponseEntity<List<DocumentType>> getAllActiveDocuments(){
        log.info("Inside ClaimDetailsController.getAllActiveDocuments");
        return new ResponseEntity<>(claimDetailsService.getAllActiveDocuments(),HttpStatus.OK);
    }
    @GetMapping("/getAllLineCategory")
    public ResponseEntity<List<DetailType>> getAllLineCategory(){
        log.info("Inside ClaimDetailsController.getAllLineCategory");
        return new ResponseEntity<>(claimDetailsService.getAllLineCategory(),HttpStatus.OK);
    }
    @GetMapping("/findDetailTypeCodeByDetailTypeId")
    public ResponseEntity<String> findDetailTypeCodeByDetailTypeId(@RequestParam(name = "detailTypeId", required = false) String detailTypeId){
        log.info("Inside ClaimDetailsController.findDetailTypeCodeByDetailTypeId");
        return new ResponseEntity<>(claimDetailsService.findDetailTypeCodeByDetailTypeId(detailTypeId),HttpStatus.OK);
    }
    @GetMapping("/getAllClaimLineAddReason")
    public ResponseEntity<List<LineAddReason>> getAllClaimLineAddReason(){
        log.info("Inside ClaimDetailsController.getAllClaimLineAddReason");
        return new ResponseEntity<>(claimDetailsService.getAllClaimLineAddReason(),HttpStatus.OK);
    }
    
    @PostMapping("/laborLineItem")
    public ResponseEntity<Void> laborLineItem(@RequestBody ClaimDocumentDTO claimDocumentDTO){
        log.info("Inside ClaimDetailsController.laborLineItem");
        claimDetailsService.laborLineItem(claimDocumentDTO);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    
    @PostMapping("/serviceLineItem")
    public ResponseEntity<Void> serviceLineItem(@RequestBody ClaimDocumentDTO claimDocumentDTO){
        log.info("Inside ClaimDetailsController.serviceLineItem");
        claimDetailsService.serviceLineItem(claimDocumentDTO);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    @GetMapping("/getClaimUnitOfMeasure")
    public ResponseEntity<List<UnitOfMeasure>> getClaimUnitOfMeasure(){
        log.info("Inside ClaimDetailsController.getClaimUnitOfMeasure");
        return new ResponseEntity<>(claimDetailsService.getClaimUnitOfMeasure(),HttpStatus.OK);
    }

    @PostMapping("/validateSmartCodeRules")
    public ResponseEntity<List<SmartCodeMessageDTO>> validateSmartCodeRules(@RequestBody ClaimLevelRulesDTO claimLevelRulesDTO) throws Exception{
        log.info("Inside ClaimDetailsController.validateSmartCodeRules");
        try {
        	return new ResponseEntity<>(smartCodeRulesService.validateSmartCodeRules(claimLevelRulesDTO),HttpStatus.OK);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/prepareRGA")
    public PrepareRGAResponseDTO prepareRGA(@RequestBody RGARequestDTO rgaRequestDTO) {
        log.info("Inside ClaimDetailsController.prepareRGA");
        PrepareRGAResponseDTO   rgaResponseDTO = claimDetailsService.prepareRGA(rgaRequestDTO);	
        return rgaResponseDTO;
    }

    @GetMapping("/fetchAuditHistoryByClaimId")
    public ResponseEntity<List<AuditHistoryDTO>> fetchAuditHistoryByClaimId(@RequestParam(name = "claimId") Integer claimId){
        log.info("Inside ClaimDetailsController.fetchAuditHistoryByClaimId");
        return new ResponseEntity<>(auditHistoryService.fetchAuditHistoryByClaimId(claimId),HttpStatus.OK);
    }
    
    @PostMapping("/routed")
    public ResponseEntity<Void> routed(@RequestBody ClaimRoutedDTO claimRoutedDTO){
        log.info("Inside UserController.routed");
        claimDetailsService.routed(claimRoutedDTO);
     return  new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("/updateSellingCompanyAndReasoCode")
    public ResponseEntity<Void> updateSellingCompanyAndReasoCode(@RequestBody UpdateReasonCodeDTO updateReasonCodeDTO) {
        log.info("Inside ClaimDetailsController.updateSellingCompanyAndReasoCode");
      claimDetailsService.updateSellingCompanyAndReasoCode(updateReasonCodeDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping("/addGenericDocument")
    public ResponseEntity<Void> addGenericDocument(@RequestBody ClaimDocumentDTO claimDocumentDTO){
        claimDetailsService.addGenericDocument(claimDocumentDTO);
        return  new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PutMapping("/updatePauseClaimNote")
    public ResponseEntity<UpdateClaimStatusResponseDTO> updatePauseClaimNote(@RequestBody UpdatePauseClaimNoteRequestDTO updatePauseClaimNoteRequestDTO) {
        log.info("Inside ClaimsController.updateClaimStatusByAction requestParam :: {}", updatePauseClaimNoteRequestDTO.getClaimId());
        UpdateClaimStatusResponseDTO response = claimDetailsService.updatePauseClaimNote(updatePauseClaimNoteRequestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
