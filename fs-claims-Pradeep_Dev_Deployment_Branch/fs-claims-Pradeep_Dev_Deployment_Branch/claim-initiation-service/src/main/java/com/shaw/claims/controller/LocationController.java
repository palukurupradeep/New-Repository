package com.shaw.claims.controller;

import com.shaw.claims.dto.LocationResponseDTO;
import com.shaw.claims.services.LocationService;
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
@RequestMapping("/location/v1")
public class LocationController {

    Logger log = LogManager.getLogger(LocationController.class);

    @Autowired
    private LocationService locationService;

    @GetMapping("/")
    public ResponseEntity<List<LocationResponseDTO>> location() {
        log.info(("LocationController.location"));
        return new ResponseEntity<>(locationService.location(), HttpStatus.OK);
    }
}
