package com.shaw.claims.controller;

import com.shaw.claims.dto.InvoiceLineTypeResponseDTO;
import com.shaw.claims.dto.InvoiceStatusResponseDTO;
import com.shaw.claims.dto.InvoiceTypeResponseDTO;
import com.shaw.claims.services.InvoiceService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/invoice/v1")
public class InvoiceController {
    Logger log = LogManager.getLogger(InvoiceController.class);

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/getAllActiveInvoiceLineType")
    public ResponseEntity<List<InvoiceLineTypeResponseDTO>> getAllActiveInvoiceLineType(){
        log.info("InvoiceController.getAllActiveInvoiceLineType");
        return new ResponseEntity<>(invoiceService.getAllActiveInvoiceLineType(),HttpStatus.OK);
    }

    @GetMapping("/getAllActiveInvoiceType")
    public ResponseEntity<List<InvoiceTypeResponseDTO>> getAllActiveInvoiceType(){
        log.info("InvoiceController.getAllActiveInvoiceType");
        return new ResponseEntity<>(invoiceService.getAllActiveInvoiceType(),HttpStatus.OK);
    }

    @GetMapping("/getAllActiveInvoiceStatus")
    public ResponseEntity<List<InvoiceStatusResponseDTO>> getAllActiveInvoiceStatus(){
        log.info("InvoiceController.getAllActiveInvoiceStatus");
        return new ResponseEntity<>(invoiceService.getAllActiveInvoiceStatus(),HttpStatus.OK);
    }
}