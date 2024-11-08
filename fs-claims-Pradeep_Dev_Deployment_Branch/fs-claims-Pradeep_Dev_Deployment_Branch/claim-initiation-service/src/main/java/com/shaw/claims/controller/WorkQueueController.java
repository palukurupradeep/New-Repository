package com.shaw.claims.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shaw.claims.dto.UserWorkQueueFieldDTO;
import com.shaw.claims.dto.WorkQueueFieldsResponseDTO;
import com.shaw.claims.dto.WorkQueueResponseDTO;
import com.shaw.claims.services.WorkQueueService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/workqueue/v1")
public class WorkQueueController {
    Logger log = LogManager.getLogger(WorkQueueController.class);
    @Autowired
    WorkQueueService workQueueService;
    @GetMapping("/getClaimWorkQueues")
    public ResponseEntity<WorkQueueResponseDTO> getClaimWorkQueues(@RequestParam(name = "userId") Integer userId){
        log.info("Inside WorkQueueController.getClaimWorkQueues");
       
        return new ResponseEntity<>(workQueueService.getClaimWorkQueues(userId), HttpStatus.OK);
    }
    @GetMapping("/getPriorityClaims")
    public ResponseEntity<WorkQueueResponseDTO> getPriorityClaims(@RequestParam(name = "userId") Integer userId){
        log.info("Inside DashBoardController.getPriorityClaims");
        return new ResponseEntity<>(workQueueService.getPriorityClaims(userId),HttpStatus.OK);
    }
    @GetMapping("/previousDayClaims")
    public ResponseEntity<Map<String, String>> previousDayClaims(@RequestParam(name = "userId") Integer userId){
        log.info("Inside DashBoardController.previousDayClaims");
        return new ResponseEntity<>(workQueueService.previousDayClaims(userId),HttpStatus.OK);
    }
    @GetMapping("/lastActivityClaims")
    public ResponseEntity<WorkQueueResponseDTO> lastActivityClaims(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "days") Integer days){
        log.info("Inside DashBoardController.lastActivityClaims");
        return new ResponseEntity<>(workQueueService.lastActivityClaims(userId,days),HttpStatus.OK);
    }
    @GetMapping("/agedClaims")
    public ResponseEntity<WorkQueueResponseDTO> agedClaims(@RequestParam(name = "userId") Integer userId,@RequestParam(name = "days") Integer days){
        log.info("Inside DashBoardController.agedClaims");
        return new ResponseEntity<>(workQueueService.agedClaims(userId,days),HttpStatus.OK);
    }
    @PostMapping("/addUserWorkQueueFields")
    public ResponseEntity<Void> addUserWorkQueueFields(@RequestBody UserWorkQueueFieldDTO userWorkQueueFieldDTO)
    {
        log.info("Inside WorkQueueController.addUserWorkQueueFields");
        workQueueService.addUserWorkQueueFields(userWorkQueueFieldDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/fetchUserWorkQueueFieldsByUserId")
    public ResponseEntity<List<WorkQueueFieldsResponseDTO>> fetchUserWorkQueueFieldsByUserId(@RequestParam(name = "createdByUserId") Integer createdByUserId){
    	log.info("Inside WorkQueueController.fetchUserWorkQueueFieldsByUserId");
    	return new ResponseEntity<>(workQueueService.fetchUserWorkQueueFieldsByUserId(createdByUserId),HttpStatus.OK);
    }
    @GetMapping("/fetchWorkQueueFields")
    public ResponseEntity<List<WorkQueueFieldsResponseDTO>> fetchWorkQueueFields(){
        log.info("Inside WorkQueueController.fetchWorkQueueFields");
        return new ResponseEntity<>(workQueueService.fetchWorkQueueFields(),HttpStatus.OK);
    }
}
