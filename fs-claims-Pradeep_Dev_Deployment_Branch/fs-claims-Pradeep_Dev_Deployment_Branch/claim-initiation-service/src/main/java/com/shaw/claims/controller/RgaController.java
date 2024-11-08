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

import com.shaw.claims.dto.ClaimRgaHeaderDTO;
import com.shaw.claims.dto.ClaimRgaReturnReceiptProcessResponseDTO;
import com.shaw.claims.dto.RgaAddressDTO;
import com.shaw.claims.dto.RgaIssueResponseDTO;
import com.shaw.claims.dto.ServiceToKeepTypeDTO;
import com.shaw.claims.dto.UpdateRGAStatusResponseDTO;
import com.shaw.claims.dto.UpdateReceiptGoodsDTO;
import com.shaw.claims.services.RgaService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/rga/v1")
public class RgaController {

	Logger log = LogManager.getLogger(RgaController.class);
	
	@Autowired
	RgaService rgaService;
	
	
	@PostMapping("/rgaDraftORSave")
	public ResponseEntity<RgaIssueResponseDTO> rgaDraftORSave(@RequestBody ClaimRgaHeaderDTO claimRgaHeaderDTO) {
		log.info("inside.RgaController.rgaDraftORSave");
	 
		return  new ResponseEntity<>(rgaService.rgaDraftORSave(claimRgaHeaderDTO),HttpStatus.OK);
	}


	
	@GetMapping("/fetchRgaDetails")
	public ResponseEntity<List<ClaimRgaHeaderDTO>> fetchRgaDetails(@RequestParam int claimId) {
		log.info("inside.RgaController.fetchRgaDetails");
		 return new ResponseEntity<>(rgaService.fetchRgaDetails(claimId),HttpStatus.OK);
		 
	}
	
	@GetMapping("/fetchRgaDetailsByClaimIdAndRgaNumber")
	public ResponseEntity<ClaimRgaHeaderDTO> fetchRgaDetailsByClaimIdAndRgaNumber(@RequestParam int claimId,String rgaNumber) {
		log.info("inside.RgaController.fetchRgaDetails");
		 return new ResponseEntity<>(rgaService.fetchRgaDetailsByClaimIdAndRgaNumber(claimId,rgaNumber),HttpStatus.OK);
		 
	}
	
	@PutMapping("/updateRGAStatusByAction")
	@Operation(description = "<h2><b>Possible actions are : Decline, Void, Redeliver,Reopen, Close.</h2></b>")
	public ResponseEntity<UpdateRGAStatusResponseDTO> updateRGAStatusByAction(@RequestParam int claimRgaHeaderId,@RequestParam int loggedInUserId, String action,String noteText) {
		 log.info("Inside RgaController.updateRGAStatusByAction requestParam :: {}, {}", claimRgaHeaderId, action, loggedInUserId);
		 return new ResponseEntity<>(rgaService.updateRGAStatusByAction(claimRgaHeaderId, action, loggedInUserId,noteText),HttpStatus.OK);
	}
	
	@PutMapping("/issueRGAValidation")
	public void issueRGAValidation(@RequestParam int claimRgaHeaderId) {
		 log.info("Inside RgaController.issueRGAValidation requestParam :: {}", claimRgaHeaderId);
		 rgaService.issueRGAValidation(claimRgaHeaderId); 
	}

	
	@GetMapping("/findRgaDetailsByRgaNumber")
	public ResponseEntity<ClaimRgaHeaderDTO> findRgaDetailsByRgaNumber(@RequestParam String rgaNumber) {
		log.info("inside.RgaController.findRgaDetailsByRgaNumber");
		 return new ResponseEntity<>(rgaService.findRgaDetailsByRgaNumber(rgaNumber),HttpStatus.OK);
		 
	}
	
	@PutMapping("/updateReceiptOfGoods")
	public void updateReceiptOfGoods(@RequestBody UpdateReceiptGoodsDTO updateReceiptGoodsDTO) {
		log.info("inside.RgaController.updateReceiptOfGoods");
	    rgaService.updateReceiptOfGoods(updateReceiptGoodsDTO);
	}

	@GetMapping("/returnReceiptProcess")
	public ResponseEntity<List<ClaimRgaReturnReceiptProcessResponseDTO>> returnReceiptProcess(@RequestParam(value = "locationCode") String locationCode, @RequestParam(value = "rgaNumber", required = false) String rgaNumber) {
		log.info("inside.RgaController.returnReceiptProcess");
		return new ResponseEntity<>(rgaService.returnReceiptProcess(locationCode,rgaNumber),HttpStatus.OK);

	}
	
	
	@PutMapping("/cancelRga")
	public void cancelRga(@RequestParam int claimId, @RequestParam String rgaNumber) {
		log.info("inside.RgaController.cancelRga");
	    rgaService.cancelRga(claimId,rgaNumber);
	}
	
	@GetMapping("/serviceToKeepType")
	public ResponseEntity<List<ServiceToKeepTypeDTO>> fetchServiceToKeepType() {
		log.info("inside.RgaController.fetchServiceToKeepType");
		 return new ResponseEntity<>(rgaService.fetchServiceToKeepType(),HttpStatus.OK);
	}
	
	@GetMapping("/fetchCountryAndState")
	public ResponseEntity<RgaAddressDTO> fetchCountryAndState(@RequestParam int countryId,@RequestParam int stateId) {
		log.info("inside.RgaController.fetchCountryAndState");
		 return new ResponseEntity<>(rgaService.fetchCountryAndState(countryId,stateId),HttpStatus.OK);
		 
	}
}
