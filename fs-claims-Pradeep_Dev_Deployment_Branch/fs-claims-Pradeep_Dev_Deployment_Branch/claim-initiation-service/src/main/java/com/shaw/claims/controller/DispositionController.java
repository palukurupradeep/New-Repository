package com.shaw.claims.controller;

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

import com.shaw.claims.dto.ClaimCrmHeaderDTO;
import com.shaw.claims.dto.ClaimDispositionHeaderDTO;
import com.shaw.claims.services.DispositionService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/disposition/v1")
public class DispositionController {
Logger log = LogManager.getLogger(DispositionController.class);
	
	@Autowired
	DispositionService dispositionService;
	
	@PostMapping("/dispositionHoldOrIssue")
	public void dispositionHoldOrIssue(@RequestBody ClaimDispositionHeaderDTO claimDispositionHeaderDTO) {
		log.info("inside.DispositionController.dispositionHoldOrIssue");
		dispositionService.dispositionHoldOrIssue(claimDispositionHeaderDTO);
	}
	
	@GetMapping("/dispositonVoid")
	public ResponseEntity<Void> DispositonVoid(@RequestParam int claimDispositionHeaderId) {
		log.info("inside.DispositionController.DispositonVoid");
		dispositionService.DispositonVoid(claimDispositionHeaderId);
		 return  new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	@GetMapping("/fetchDispositonDetailsByClaimIdAndDispositonNumber")
	public ResponseEntity<ClaimDispositionHeaderDTO> fetchDispositonDetailsByClaimIdAndDispositonNumber(@RequestParam int claimId,String dispositionNumber) {
		log.info("inside.DispositionController.fetchDispositonDetailsByClaimIdAndDispositonNumber");
		 return new ResponseEntity<>(dispositionService.fetchDispositonDetailsByClaimIdAndDispositonNumber(claimId,dispositionNumber),HttpStatus.OK);
		 
	}
}

