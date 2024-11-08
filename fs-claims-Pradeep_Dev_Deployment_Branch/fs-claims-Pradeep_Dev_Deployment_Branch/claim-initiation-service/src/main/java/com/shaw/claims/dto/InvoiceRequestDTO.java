package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InvoiceRequestDTO {
    private String customerNumber;
    private String invoiceNumber;

    public InvoiceRequestDTO(String customerNumber, String invoiceNumber) {
        this.customerNumber = customerNumber;
        this.invoiceNumber = invoiceNumber;
    }
}
