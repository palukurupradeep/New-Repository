package com.shaw.claims.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.SearchClaimHistoryRequestDTO;
import com.shaw.claims.dto.SearchClaimHistoryResponseDTO;
import com.shaw.claims.dto.SecondarySearchDTO;
import com.shaw.claims.model.ClaimStatus;
import com.shaw.claims.model.Lookup;
import com.shaw.claims.model.WorkStatus;
import com.shaw.claims.services.SearchClaimHistoryService;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/searchclaimhistory/v1")
public class SearchClaimHistoryController {
	Logger log = LogManager.getLogger(SearchClaimHistoryController.class);
	
	@Autowired
	SearchClaimHistoryService searchClaimHistoryService;

	 @PostMapping("/searchClaimHistory")
	    public ResponseEntity<List<SearchClaimHistoryResponseDTO>> searchClaimHistory(@RequestBody SearchClaimHistoryRequestDTO searchClaimHistoryRequest ){
	        log.info("Inside SearchClaimHistoryController.searchClaimHistory");
	        return new ResponseEntity<>(searchClaimHistoryService.searchClaimHistory(searchClaimHistoryRequest), HttpStatus.OK);
	    }
	 
	 @GetMapping("/getClaimStatus")
	    public ResponseEntity<List<ClaimStatus>> getClaimStatus(){
		 log.info("Inside SearchClaimHistoryController.getClaimStatus");
	    	List<ClaimStatus> claimStatus = searchClaimHistoryService.getClaimStatus();
	        return new ResponseEntity<>(claimStatus, HttpStatus.OK);
	    }
	 
	 @GetMapping("/getPrimarySearch")
	   public ResponseEntity<List<Lookup>> getPrimarySearch(){
			 log.info("Inside SearchClaimHistoryController.getPrimarySearch");
		    	List<Lookup> primarySearch = searchClaimHistoryService.getPrimarySearch();
		        return  new ResponseEntity<>(primarySearch, HttpStatus.OK);
		    }
	 
	 @GetMapping("/getSecondarySearch")
	    public ResponseEntity<List<SecondarySearchDTO>> getSecondarySearch(@RequestParam String primarySearchType) {
	        log.info("Inside SearchClaimHistoryController.getSecondarySearch with primarySearchType: {}", primarySearchType);
	        List<SecondarySearchDTO> secondarySearchDTO = searchClaimHistoryService.getSecondarySearch(primarySearchType);
	        return new ResponseEntity<>(secondarySearchDTO, HttpStatus.OK);
	    }
	
	 @PostMapping("/exportToExcel")
	    public ResponseEntity<byte[]> exportToExcel(@RequestBody SearchClaimHistoryRequestDTO searchClaimHistoryRequest) throws IOException {
	        byte[] excelData = searchClaimHistoryService.exportToExcel(searchClaimHistoryRequest);

	        if (excelData == null) {
	            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	        }

	        HttpHeaders headers = new HttpHeaders();
	        headers.add("Content-Disposition", "attachment; filename=claim-history.xlsx");

	        return ResponseEntity.ok()
	                .headers(headers)
	                .body(excelData);
	    }
}
