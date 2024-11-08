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

import com.shaw.claims.dto.SampleRequestDTO;
import com.shaw.claims.dto.SampleReviewDTO;
import com.shaw.claims.model.ClaimReasonDefinition;
import com.shaw.claims.services.SampleReviewService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/samplereview/v1")
public class SampleReviewController {

	Logger log = LogManager.getLogger(SampleReviewController.class);
	
	@Autowired
	SampleReviewService sampleReviewService;
	
		@PostMapping("/sampleRequest")
	public void sampleRequest(@RequestBody SampleRequestDTO sampleRequestDTO) {
		log.info("inside.SampleReviewController.sampleRequest");
		sampleReviewService.sampleRequest(sampleRequestDTO);
	}

	@GetMapping("/fetchSampleSizeRequirementAndTesting")
	public ResponseEntity<ClaimReasonDefinition> fetchSampleSizeRequirementAndTesting(@RequestParam String reasonCode) {
		log.info("inside.SampleReviewController.fetchSampleSizeRequirementAndTesting");
		return new ResponseEntity<>(sampleReviewService.fetchSampleSizeRequirementAndTesting(reasonCode),HttpStatus.OK);

	}
	
	
	@PutMapping("/updateSampleReviewByAction")
	@Operation(description = "<h2><b>Possible actions are : Approved, Declined,SampleReceived</h2></b>")
	public void updateSampleReviewByAction(@RequestBody SampleReviewDTO sampleReviewDTO) {
		log.info("inside.SampleReviewController.updateSampleReviewByAction");
		sampleReviewService.updateSampleReviewByAction(sampleReviewDTO);
	}
	
	
}


