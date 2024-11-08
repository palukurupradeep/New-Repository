package com.shaw.claims.controller;

import com.shaw.claims.dto.AddClaimVendorDTO;
import com.shaw.claims.dto.ClaimVendorDetailResponseDTO;
import com.shaw.claims.dto.UpdateClaimVendorRequestDTO;
import com.shaw.claims.services.ClaimVendorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/claimvendor/v1")
public class ClaimVendorController {

    Logger log = LogManager.getLogger(CreditMemoController.class);

    @Autowired
    private ClaimVendorService claimVendorService;

    @PostMapping("/addClaimVendor")
    public ResponseEntity<Void> addClaimVendor(@RequestBody AddClaimVendorDTO addClaimVendorDTO) throws ParseException {
        log.info("ClaimVendorController.addClaimVendor");
        claimVendorService.addClaimVendor(addClaimVendorDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getClaimVendorDetail")
    public ResponseEntity<ClaimVendorDetailResponseDTO> getClaimVendorDetail(@RequestParam(name = "claimVendorNumber") String claimVendorNumber) {
        log.info("ClaimVendorController.getClaimVendorDetail");
        return new ResponseEntity<>(claimVendorService.getClaimVendorDetail(claimVendorNumber),HttpStatus.OK);
    }

    @PutMapping("/updateClaimVendor")
    public ResponseEntity<Void> updateClaimVendor(@RequestBody UpdateClaimVendorRequestDTO claimVendorRequestDTO) throws ParseException {
        log.info("ClaimVendorController.updateClaimVendor");
        claimVendorService.updateClaimVendor(claimVendorRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}