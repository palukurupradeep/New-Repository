package com.shaw.claims.controller;

import com.shaw.claims.dto.ClaimNoteRequestDTO;
import com.shaw.claims.dto.InStoreClaimResponseDTO;
import com.shaw.claims.dto.InStoreClaimsResponseDTO;
import com.shaw.claims.services.InStoreClaimService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/instoreclaim/v1")
public class InStoreClaimController {

    Logger log = LogManager.getLogger(InStoreClaimController.class);

    @Autowired
    private InStoreClaimService inStoreClaimService;

    @GetMapping("/inStoreClaimsList")
    public ResponseEntity<List<InStoreClaimsResponseDTO>> inStoreClaimsList(@RequestParam(name = "region") String region,
                                                                            @RequestParam(name = "territory") String territory,
                                                                            @RequestParam(name = "division",required = false) String division) {
        log.info("Inside ClaimDetailsController.inStoreClaimsList");
        return new ResponseEntity<>(inStoreClaimService.inStoreClaimsList(region,territory,division),HttpStatus.OK);
    }

    @GetMapping("/inStoreClaimNumber")
    public ResponseEntity<InStoreClaimResponseDTO> inStoreClaimByClaimNumber (@RequestParam("claimNumber") String claimNumber) {
        log.info("Inside ClaimDetailsController.claimByClaimNumber");
        return ResponseEntity.status(HttpStatus.OK).body(inStoreClaimService.inStoreClaimByClaimNumber(claimNumber));
    }
}
