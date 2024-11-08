package com.shaw.claims.controller;

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

import com.shaw.claims.dto.WorkHistoryDTO;
import com.shaw.claims.services.WorkHistoryService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/workhistory/v1")
public class WorkHistoryController {
    Logger log = LogManager.getLogger(WorkHistoryController.class);
    @Autowired
    WorkHistoryService workHistoryService;
    @GetMapping("/getWorkHistory")
    public ResponseEntity<List<WorkHistoryDTO>> getWorkHistory(@RequestParam(name = "userId") Integer userId){
        log.info("Inside WorkHistoryController.getWorkHistory");
        List<WorkHistoryDTO> workHistoryList =  workHistoryService.getWorkHistory(userId);
        
        return new ResponseEntity<>(workHistoryList, HttpStatus.OK);
    }
}
