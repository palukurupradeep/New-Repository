package com.onbase.controller;

import com.onbase.service.OnBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v1")
public class OnBaseController {
    @Autowired
    OnBaseService service;
    @GetMapping("/getClaimCount")
    public ResponseEntity<Integer> getClaimCount(@RequestParam(name = "activeDirId") String activeDirId){
        Integer claimCount = service.getClaimCount(activeDirId);
        return new ResponseEntity<>(claimCount, HttpStatus.OK);
    }
}
