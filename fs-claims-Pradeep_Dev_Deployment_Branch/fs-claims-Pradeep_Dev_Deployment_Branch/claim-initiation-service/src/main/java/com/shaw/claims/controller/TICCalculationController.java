package com.shaw.claims.controller;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.LaborRateDTO;
import com.shaw.claims.dto.TicCalculationResponse;
import com.shaw.claims.services.TicCalculationService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ticCalculation/v1")
public class TICCalculationController {
    Logger log = LogManager.getLogger(TICCalculationController.class);
  
    @Autowired
    TicCalculationService service;
    
    @GetMapping("/ticCalculation")
    public ResponseEntity<TicCalculationResponse> ticCalculation(@RequestParam(name = "claimId") Integer claimId, @RequestParam(name = "claimLineId", required = false) Integer claimLineId, @RequestParam(name = "estimateAmount", required = false) BigDecimal estimateAmount ){
        log.info("Inside WorkHistoryController.ticCalculation");
        TicCalculationResponse ticCalculation =  service.ticCalculations(claimId,  claimLineId, estimateAmount);
        return new ResponseEntity<>(ticCalculation, HttpStatus.OK);
    }
    
    @GetMapping("/findAllLaborRates")
    public ResponseEntity<List<LaborRateDTO>> findAllLaborRates(){
        log.info("Inside WorkHistoryController.findAllLaborRates");
        List<LaborRateDTO> laborRatesList =  service.findAllLaborRates();
        return new ResponseEntity<>(laborRatesList, HttpStatus.OK);
    }
}
