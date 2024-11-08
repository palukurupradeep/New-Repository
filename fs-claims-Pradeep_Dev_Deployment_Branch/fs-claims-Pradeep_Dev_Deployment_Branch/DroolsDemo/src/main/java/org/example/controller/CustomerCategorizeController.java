package org.example.controller;


import org.example.model.CustomerRequest;
import org.example.model.CustomerType;
import org.example.service.CustomerCategorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/getCustomerType")
public class CustomerCategorizeController {

    @Autowired
    private CustomerCategorizeService customerCategorizeService;

    @PostMapping
    public ResponseEntity<CustomerType> getCustomer(@RequestBody CustomerRequest customerRequest) {
        customerRequest.setTodaysDate(new Date());
        String carpetRange = customerRequest.getCarpetRange();
        if (carpetRange!=null) {
            customerRequest.setCarpetStartRange(Integer.valueOf(carpetRange.substring(0, carpetRange.indexOf("-"))));
            customerRequest.setCarpetEndRange(Integer.valueOf(carpetRange.substring(carpetRange.indexOf("-") + 1, carpetRange.length())));
        }
        CustomerType customerType = customerCategorizeService.getCustomerType(customerRequest);
        long diff = customerRequest.getTodaysDate().getTime() - customerRequest.getInvoiceDate().getTime();
        System.out.println("Days: " + diff / 1000 / 60 / 60 / 24);
        return new ResponseEntity<>(customerType, HttpStatus.OK);
    }
}
