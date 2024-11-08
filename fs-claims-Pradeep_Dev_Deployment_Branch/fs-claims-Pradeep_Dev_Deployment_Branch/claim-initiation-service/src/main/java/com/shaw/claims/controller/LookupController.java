package com.shaw.claims.controller;

import com.shaw.claims.model.Lookup;
import com.shaw.claims.services.LookupService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/lookup/v1")
public class LookupController {
    Logger log = LogManager.getLogger(LookupController.class);
    @Autowired
    LookupService lookupService;
    @GetMapping("/getLookupTypeByCode")
    public ResponseEntity<List<Lookup>> getLookupTypeByCode(String lookupTypeCode){
        log.info("Inside LookupController.getLookupTypeByCode");
        List<Lookup> lookup = lookupService.getLookupTypeByCode(lookupTypeCode);
        return new ResponseEntity<>(lookup, HttpStatus.OK);
    }
}
