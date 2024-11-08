package com.shaw.claims.controller;

import com.shaw.claims.dto.GeoLocationDTO;
import com.shaw.claims.services.AzureMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/maps/v1")
public class AzureMapController {

    @Autowired
    AzureMapService mapService;

    @GetMapping("/getLocation")
    public ResponseEntity<String> getBatchStatus(@RequestParam(value = "address") String address) {
        String [] addr11 = {"5440 BUSINESS PARKWAY THEODORE AL 36582","7310 W. ROOSEVELT ST. SUITE 44 PHOENIX AZ 85043","11411 VALLEY VIEW ST. CYPRESS CA 90630","4025 WHIPPLE RD. UNION CITY CA 94587","10300 EAST 40TH AVENUE DENVER CO 80238","10670 CENTRAL PORT DRIVE, SUITE #100 ORLANDO FL 32824","2410 GALVIN DRIVE ELGIN IL 60124","16955 W. 116TH STREET LENEXA KS 66219","275 JOHN HANCOCK RD SUITE 100 TAUNTON MA 02780","1405 MAGELLAN RD HANOVER MD	21076","41133 VAN BORN RD. STE 205 BELLEVILLE MI 48111","126 ENTERPRISE DRIVE WENTZVILLE MO	63385","10901 TEXLAND BLVD CHARLOTTE NC 28273","1267 S. RIVER ROAD SUITE 100 CRANBURY NJ 08512","8000 BLUEWATER NW - SUITE C ALBUQUERQUE NM 87121","7015 CORPORATE PLAZA DR, SUITE 130 LAS VEGAS NV 89118","8580 SEWARD RD  SUITE 400 FAIRFIELD OH 45014","15925 N. LOMBARD ST., SUITE 200 PORTLAND OR 972036811","4905 SOUTHRIDGE BLVD. STE 1 MEMPHIS TN 38141","7401 SECURITY WAY SUITE 100 JERSEY VILLAGE TX 77040","6300 N. LOOP 1604 EAST SAN ANTONIO TX 78247","22620 64TH AVE. S. KENT WA 98032","SUITE 500 SALT LAKE CITY UT 84104","3429 Carpet Capital Drive Dalton GA 30720","595 Northport PKWY Savannah GA 31407","2456 Yeager Drive SW Dalton GA 30720","947 Union Grove Road Adairsville GA 30103","952 Industrial Boulevard Ringgold GA 30736","3104 COUNTY RD # 6 ELKHART SHAW INDUSTRIES, INC. 46514","780 BRICKWORKS DRIVE LEETSDALE OR 15056","400 DIVIDEND DRIVE SUITE #150 COPPELL TX 75019","4601 NW 3RD STREET OKLAHOMA CITY OH 73127","2841 Beverly Dr Eagan UT 55121"};
        String [] addr = {"5440 BUSINESS PARKWAY THEODORE AL 36582","7310 W. ROOSEVELT ST. SUITE 44 PHOENIX AZ 85043","11411 VALLEY VIEW ST. CYPRESS CA 90630","4025 WHIPPLE RD. UNION CITY CA 94587","10300 EAST 40TH AVENUE DENVER CO 80238","10670 CENTRAL PORT DRIVE, SUITE #100 ORLANDO FL 32824","2410 GALVIN DRIVE ELGIN IL 60124","3104 COUNTY RD # 6 ELKHART IN 46514","16955 W. 116TH STREET LENEXA KS 66219","275 JOHN HANCOCK RD SUITE 100 TAUNTON MA 02780","1405 MAGELLAN RD HANOVER MD 21076","41133 VAN BORN RD. STE 205 BELLEVILLE MI 48111","126 ENTERPRISE DRIVE WENTZVILLE MO 63385","10901 TEXLAND BLVD CHARLOTTE NC 28273","1267 S. RIVER ROAD SUITE 100 CRANBURY NJ 08512","8000 BLUEWATER NW - SUITE C ALBUQUERQUE NM 87121","7015 CORPORATE PLAZA DR, SUITE 130 LAS VEGAS NV 89118","8580 SEWARD RD SUITE 400 FAIRFIELD OH 45014","4601 NW 3RD STREET OKLAHOMA CITY OK 73127","15925 N. LOMBARD ST., SUITE 200 PORTLAND OR 972036811","780 BRICKWORKS DRIVE LEETSDALE PA 15056","4905 SOUTHRIDGE BLVD. STE 1 MEMPHIS TN 38141","7401 SECURITY WAY SUITE 100 JERSEY VILLAGE TX 77040","6300 N. LOOP 1604 EAST SAN ANTONIO TX 78247","400 DIVIDEND DRIVE SUITE #150 COPPELL TX 75019","22620 64TH AVE. S. KENT WA 98032","SUITE 500 SALT LAKE CITY UT 84104","2841 Beverly Dr Eagan MN 55121","3429 Carpet Capital Drive Dalton GA 30720","595 Northport PKWY Savannah GA 31407","2456 Yeager Drive SW Dalton GA 30720","947 Union Grove Road Adairsville GA 30103","952 Industrial Boulevard Ringgold GA 30736"};
        List<String> addrLst = Arrays.asList(addr);
        String loc = mapService.getLocation(addrLst);
        return new ResponseEntity<>(loc, HttpStatus.OK);
    }

    @GetMapping("/getCoordinates")
    public ResponseEntity<String> getCoordinates(@RequestParam(value = "address") String address) {
        String loc = mapService.getCoordinates(address);
        return new ResponseEntity<>(loc, HttpStatus.OK);
    }

    @PostMapping("/updateLocation")
    public ResponseEntity<Void> updateLocation(@RequestParam(value = "locationId") Integer locationId) {
        mapService.updateLocation(locationId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/findClosestRDC")
    public ResponseEntity<GeoLocationDTO> findClosestRDC(@RequestBody GeoLocationDTO origin) {
        GeoLocationDTO geoLocation = mapService.findClosestRDC(origin);
        return new ResponseEntity<>(geoLocation, HttpStatus.OK);
    }
}
