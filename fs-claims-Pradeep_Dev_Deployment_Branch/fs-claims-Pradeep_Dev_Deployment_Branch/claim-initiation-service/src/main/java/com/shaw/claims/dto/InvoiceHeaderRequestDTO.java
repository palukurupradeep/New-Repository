package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InvoiceHeaderRequestDTO {

    private String customerNumber;
    private String documentNumber;
    private String documentDate;

    public InvoiceHeaderRequestDTO(String customerNumber, String documentNumber, String documentDate) {
        this.customerNumber = customerNumber;
        this.documentNumber = documentNumber;
        this.documentDate = documentDate;
    }
}
