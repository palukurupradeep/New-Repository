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

import com.shaw.claims.dto.LocationDTO;
import com.shaw.claims.services.LocationService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/location/v1")
public class LocationController {
    Logger log = LogManager.getLogger(LocationController.class);
    @Autowired
    LocationService locationService;
    @GetMapping("/fetchLocationsByLocationCode")
    public ResponseEntity<List<LocationDTO>> fetchLocationsByLocationCode(@RequestParam(name = "locationCode") String locationCode){
        log.info("Inside LocationService.fetchLocationsByLocationCode");
        return new ResponseEntity<>(locationService.fetchLocationsByLocationCode(locationCode), HttpStatus.OK);
    }
  
}
