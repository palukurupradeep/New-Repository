package com.shaw.claims.controller;

import com.shaw.claims.dto.*;
import com.shaw.claims.model.*;
import com.shaw.claims.services.ClaimCategoryService;
import com.shaw.claims.services.ClaimReasonDefinitionService;
import com.shaw.claims.services.ClaimService;
import com.shaw.claims.services.DocumentTypeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/claim/v1")
public class ClaimController {
    Logger log = LogManager.getLogger(ClaimController.class);

    @Autowired
    ClaimCategoryService claimCategoryService;

    @Autowired
    ClaimService claimsService;
    @Autowired
    DocumentTypeService documentTypeService;

    @Autowired
    ClaimReasonDefinitionService claimReasonDefinitionService;

    @GetMapping("/getClaimCategories")
    public ResponseEntity<List<ClaimCategory>> getClaimCategories() {
        log.info("Inside ClaimController.getClaimCategories");
        List<ClaimCategory> claimCategory = claimCategoryService.getClaimCategories();
        return new ResponseEntity<>(claimCategory, HttpStatus.OK);
    }

    @GetMapping("/getReasonCodeByCategory/{categoryId}")
    public ResponseEntity<List<ClaimReasonDefinition>> getReasonCodeByCategory(@PathVariable("categoryId") Integer categoryId) {
        log.info("Inside ClaimController.getReasonCodeByCategory");
        List<ClaimReasonDefinition> claimCategory = claimReasonDefinitionService.getReasonCodeByCategory(categoryId);
        return new ResponseEntity<>(claimCategory, HttpStatus.OK);
    }

    @PostMapping("/initiate")
    public ResponseEntity<ClaimInitiationResponseDTO> initiate(@RequestBody ClaimDTO claimDTO){
        log.info("Inside ClaimsController.initiate requestBody :: {}", claimDTO);
        ClaimInitiationResponseDTO claimInitiationResponse =  claimsService.initiate(claimDTO);
        return ResponseEntity.ok().body(claimInitiationResponse);
    }

    @PostMapping("/claimInitiate")
    public ResponseEntity<ClaimInitiationResponseDTO> claimInitiate(@RequestBody ClaimDTO claimDTO){
        log.info("Inside ClaimsController.claimInitiate requestBody :: {}", claimDTO);
        ClaimInitiationResponseDTO claimInitiationResponse =  claimsService.claimInitiate(claimDTO);
        return ResponseEntity.ok().body(claimInitiationResponse);
    }

    @PostMapping("/claimSubmittal")
    public ResponseEntity<ResponseDTO> claimSubmittal(@RequestBody ClaimSubmittalDTO claimSubmittalDTO){
        log.info("Inside ClaimsController.claimSubmittal requestBody :: {}", claimSubmittalDTO);
        ResponseDTO responseDTO = claimsService.claimSubmittal(claimSubmittalDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PostMapping("/claimAssignment")
    public ResponseEntity<Void> claimAssignment(@RequestParam(name = "claimId") Integer claimId, @RequestParam(name = "claimUserId") Integer claimUserId) {
        log.info("Inside ClaimsController.claimAssignment requestParam :: {}", claimId, claimUserId);
        claimsService.claimAssignment(claimId,claimUserId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getStatesByCountryId/{countryId}")
    public ResponseEntity<List<State>> getStatesByCountryId(@PathVariable("countryId") Integer countryId){
        List<State> states = claimsService.getStatesByCountryId(countryId);
        return new ResponseEntity<>(states, HttpStatus.OK);
    }
    @GetMapping("/getCountries")
    public ResponseEntity<List<Country>> getCountries(){
        List<Country> countries = claimsService.getCountries();
        return new ResponseEntity<>(countries, HttpStatus.OK);
    }
    @GetMapping("/getDialCodes")
    public ResponseEntity<List<DialCode>> getDialCodes(){
        List<DialCode> dialCodes = claimsService.getDialCodes();
        return new ResponseEntity<>(dialCodes, HttpStatus.OK);
    }
    @PostMapping("/getPriorClaimSummary")
    public ResponseEntity<List<PriorClaimSummaryResponseDTO>> getPriorClaimSummary(@RequestBody PClaimDTO claimDto) {
    	log.info("Inside ClaimController.getPriorClaimSummary :: {}", claimDto);
    	List<PriorClaimSummaryResponseDTO> claimSummary = claimsService.getPriorClaimSummary(claimDto);
    	return new ResponseEntity<>(claimSummary, HttpStatus.OK);
    }
    @PostMapping("/automaticAssignment")
    public void automaticAssignment(@RequestBody List<Integer> claimId) {
        log.info(("Inside ClaimController.automaticAssignment"));
        claimsService.automaticAssignment(claimId);
    }
    @GetMapping("/getClaimStatusById")
    public ResponseEntity<ClaimStatus> getClaimStatusById(@RequestParam(name = "claimStatusId") Integer claimStatusId){
        ClaimStatus claimStatus = claimsService.getClaimStatusById(claimStatusId);
        return new ResponseEntity<>(claimStatus, HttpStatus.OK);
    }
    @GetMapping("/getWorkStatusById")
    public ResponseEntity<WorkStatusDTO> getWorkStatusById(@RequestParam(name = "workStatusId") Integer workStatusId, @RequestParam(name = "userId", required = false) Integer userId){
    	WorkStatusDTO workStatus = claimsService.getWorkStatusById(workStatusId, userId);
        return new ResponseEntity<>(workStatus, HttpStatus.OK);
    }

    @PostMapping("/merge")
    public void merge(@RequestBody MergedAndUnMergedRequestDTO mergedAndUnMergedRequestDTO){
        log.info("Inside ClaimController.merge");
        claimsService.merge(mergedAndUnMergedRequestDTO);
    }

    @PostMapping("/unMerge")
    public void unMerge(@RequestBody MergedAndUnMergedRequestDTO mergedAndUnMergedRequestDTO){
        log.info("Inside ClaimController.unMerge");
        claimsService.unMerge(mergedAndUnMergedRequestDTO);
    }
/*    @GetMapping("/getContactDetails")
    public ResponseEntity<Void> getContactDetails(@RequestParam(name = "customerNumber") String customerNumber){
        claimsService.getContactDetails(customerNumber);
        return new ResponseEntity<>(HttpStatus.OK);
    }*/

    @PutMapping("/updateClaimAddress")
    public ResponseEntity<ClaimUpdateAddressDTO> updateClaimAddress(@RequestBody ClaimUpdateAddressDTO claimUpdateAddressDTO) {
        log.info("Inside ClaimsController.updateClaimAddress requestBody :: {}", claimUpdateAddressDTO);
        ClaimUpdateAddressDTO claimUpdateAddressResponseDTO = claimsService.updateClaimAddress(claimUpdateAddressDTO);
        return new ResponseEntity<>(claimUpdateAddressResponseDTO,HttpStatus.OK);
    }

    @DeleteMapping("/document")
    public void deleteDocument(@RequestParam(name = "claimDocumentId") int claimDocumentId,@RequestParam(name = "loggedInUserId") int loggedInUserId){
        log.info("Inside ClaimController.deleteDocument");
        claimsService.deleteDocument(claimDocumentId,loggedInUserId);
    }

    @GetMapping("/unMergedClaims")
    public ResponseEntity<List<MergedUnMergedResponseDTO>> unMergedClaims(@RequestParam(name = "parentClaimId") int parentClaimId,@RequestParam(name = "customerNumber") String customerNumber){
        log.info("Inside ClaimsController.unMergedClaims");
        List<MergedUnMergedResponseDTO> mergedUnMergedResponseDTOS = claimsService.unMergedClaims(parentClaimId,customerNumber);
        return new ResponseEntity<>(mergedUnMergedResponseDTOS,HttpStatus.OK);
    }

    @GetMapping("/mergedClaims")
    public ResponseEntity<List<MergedUnMergedResponseDTO>> mergedClaims(@RequestParam(name = "parentClaimId") int parentClaimId){
        log.info("Inside ClaimsController.mergedClaims");
        List<MergedUnMergedResponseDTO> mergedUnMergedResponseDTOS = claimsService.mergedClaims(parentClaimId);
        return new ResponseEntity<>(mergedUnMergedResponseDTOS,HttpStatus.OK);
    }

    @PostMapping("/associateDocument")
    public ResponseEntity<ClaimDocument> claimAssociateDocument(@RequestBody AssociateDocumentDTO associateDocumentDTO){
        log.info("Inside ClaimController.claimAssociateDocument");
        ClaimDocument claimDocument = claimsService.claimAssociateDocument(associateDocumentDTO);
        return new ResponseEntity<>(claimDocument,HttpStatus.OK);
    }

    @GetMapping("/getWorkStatus")
    public ResponseEntity<List<WorkStatus>> getWorkStatus(){
    	List<WorkStatus> workStatus = claimsService.getWorkStatus();
        return new ResponseEntity<>(workStatus, HttpStatus.OK);
    }

    @PostMapping("/claimRoute")
    public void claimRoute(@RequestBody ClaimRouteRequestDTO claimRouteRequestDTO){
        log.info("Inside ClaimController.claimRoute");
        claimsService.claimRoute(claimRouteRequestDTO);
    }
    @GetMapping("/getDocIdByDocumentTypeCode")
    public ResponseEntity<Integer> getDocIdByDocumentTypeCode(@RequestParam(name = "documentTypeCode") String documentTypeCode){
        return new ResponseEntity<>(documentTypeService.getDocIdByDocumentTypeCode(documentTypeCode), HttpStatus.OK);
    }

    @GetMapping("/claim")
    public ResponseEntity<?> getClaim(@RequestParam(name = "claimNumber") String claimNumber,
                                      @RequestParam(name = "type") String type){
        log.info("Inside ClaimController.getClaim");
        return new ResponseEntity<>(claimsService.getClaim(claimNumber,type), HttpStatus.OK);
    }

}

