package com.shaw.claims.controller;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shaw.claims.dto.ClaimBatchDetailsDTO;
import com.shaw.claims.dto.ClaimBatchHeaderDTO;
import com.shaw.claims.model.BatchStatus;
import com.shaw.claims.services.ClaimBatchHeaderService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/claimbatch/v1")
public class ClaimBatchHeaderController {

    Logger log = LogManager.getLogger(ClaimBatchHeaderController.class);

    @Autowired
    private ClaimBatchHeaderService batchHeaderService;

    @PostMapping("/create")
    public void create(@RequestBody List<ClaimBatchHeaderDTO> claimBatchHeaderDTOS){
        log.info("inside.claimBatchHeaderController.batchInitiate");
        batchHeaderService.create(claimBatchHeaderDTOS);
    }

    
    @GetMapping("/getBatchStatus")
	public ResponseEntity<List<BatchStatus>> getBatchStatus() {
		log.info("Inside claimBatchHeaderController.getBatchStatus");

		return new ResponseEntity<>(batchHeaderService.getBatchStatus(), HttpStatus.OK);
	}
    
    @GetMapping("/getClaimBatchDetailByBatchHeaderId")
	public ResponseEntity<List<ClaimBatchDetailsDTO>> getClaimBatchDetail(@RequestParam Integer claimBatchHeaderId) {
		log.info("Inside claimBatchHeaderController.getClaimBatchDetail");

		return new ResponseEntity<>(batchHeaderService.getClaimBatchDetail(claimBatchHeaderId), HttpStatus.OK);
	}


    @PostMapping("/fetchClaimBatchHeader")
    public ResponseEntity<List<ClaimBatchHeaderDTO>> fetchClaimBatchHeader(@RequestBody ClaimBatchHeaderDTO claimBatchHeaderDTO){
        log.info("inside.claimBatchHeaderController.fetchClaimBatchHeader");
        return new ResponseEntity<>(batchHeaderService.fetchClaimBatchHeader(claimBatchHeaderDTO), HttpStatus.OK);
    }

}
