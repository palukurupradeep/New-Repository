package com.shaw.claims.controller;

import com.shaw.claims.dto.ClaimNoteRequestDTO;
import com.shaw.claims.dto.ResponseDTO;
import com.shaw.claims.services.ClaimNoteServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/claimnote/v1")
public class ClaimNoteController {

    Logger log = LogManager.getLogger(ClaimNoteController.class);

    @Autowired
    private ClaimNoteServiceImpl claimNoteService;

    @PostMapping("/claimNote")
    public ResponseEntity<ResponseDTO> claimNote(@RequestBody ClaimNoteRequestDTO claimNoteRequestDTO) {
        log.info("ClaimController.claimNote");
        return new ResponseEntity<>(claimNoteService.claimNote(claimNoteRequestDTO), HttpStatus.CREATED);
    }
}
