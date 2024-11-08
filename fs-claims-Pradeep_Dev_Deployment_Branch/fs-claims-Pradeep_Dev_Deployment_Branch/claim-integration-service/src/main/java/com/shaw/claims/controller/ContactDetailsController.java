package com.shaw.claims.controller;

import com.shaw.claims.model.ContactDetails;
import com.shaw.claims.services.ContactDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/contacts/v1")
public class ContactDetailsController {
    @Autowired
    private ContactDetailsService service;

    @GetMapping("/getContactDetails")
    public ResponseEntity<List<ContactDetails>> getContactDetails(
            @RequestParam String customerNumber, @RequestParam(required = false) String contactType){
        return new ResponseEntity<>(service.getContactDetails(customerNumber, contactType), HttpStatus.OK);
    }
}
