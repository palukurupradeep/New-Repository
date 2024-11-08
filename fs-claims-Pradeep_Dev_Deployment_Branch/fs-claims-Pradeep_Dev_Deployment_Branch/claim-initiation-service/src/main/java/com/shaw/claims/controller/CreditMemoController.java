package com.shaw.claims.controller;

import com.shaw.claims.dto.CreditMemoRequestDTO;
import com.shaw.claims.dto.RestockFeeRequestDTO;
import com.shaw.claims.dto.RestockFeeResponseDTO;
import com.shaw.claims.services.CreditMemoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/crm/v1")
public class CreditMemoController {

    Logger log = LogManager.getLogger(CreditMemoController.class);

    @Autowired
    private CreditMemoService creditMemoService;

    @PostMapping("/prepareCreditMemo")
    public ResponseEntity<Void> prepareCreditMemo(@RequestBody CreditMemoRequestDTO creditMemoRequestDTO){
        log.info("CreditMemoController.prepareCreditMemo");
        creditMemoService.prepareCreditMemo(creditMemoRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/restockFee")
    public ResponseEntity<RestockFeeResponseDTO> restockFee(@RequestBody RestockFeeRequestDTO restockFeeRequestDTO){
        log.info("CreditMemoController.restockFee");
        return new ResponseEntity<>(creditMemoService.restockFee(restockFeeRequestDTO),HttpStatus.OK);
    }
}
