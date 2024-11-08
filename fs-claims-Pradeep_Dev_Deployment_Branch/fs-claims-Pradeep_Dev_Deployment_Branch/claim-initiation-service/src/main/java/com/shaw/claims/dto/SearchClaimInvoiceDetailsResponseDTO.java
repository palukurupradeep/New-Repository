package com.shaw.claims.dto;

import lombok.Data;

@Data
public class SearchClaimInvoiceDetailsResponseDTO {
    public String customerNumber;
    public String billtoName;
    public String invoiceNbr;
    public String invoiceDate;
    public String bolNumber;
    public String orderNumber;
    public String orderDate;
    public String invType;
    public String sellco;
    public double totalTaxAmt;
    public double totalInvoiceAmt;
    public String shipToState;
    private String pONbr;
}
