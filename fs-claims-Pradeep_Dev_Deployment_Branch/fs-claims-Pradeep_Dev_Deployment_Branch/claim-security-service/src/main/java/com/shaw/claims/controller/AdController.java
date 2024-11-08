package com.shaw.claims.controller;

import com.shaw.claims.dto.ADUserDTO;
import com.shaw.claims.services.AdService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ad/v1")
public class AdController {
    Logger log = LogManager.getLogger(AdController.class);
    @Autowired
    AdService service;
    @GetMapping("/fetchUserFromAD")
    public ResponseEntity<List<ADUserDTO>> fetchUserFromAD(@RequestParam(name = "firstName", required = false) String firstName, @RequestParam(name = "lastName", required = false) String lastName, @RequestParam(name = "activeDirId", required = false) String activeDirId) throws Exception {
        log.info("Inside AdController.fetchUserFromAD");
        List<ADUserDTO> userList = service.fetchUserFromAD(firstName, lastName, activeDirId);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PutMapping("/syncUsersFromAD")
    public ResponseEntity<Void> syncUsersFromAD(@RequestParam(name = "userId", required = true) Integer userId) throws Exception {
        log.info("Inside AdController.syncUsersFromAD");
        service.syncUsersFromAD(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
