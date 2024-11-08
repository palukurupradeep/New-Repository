package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceHeaderResponseDTO {
    public String customerNumber;
    public String invoiceNbr;
    public String invoiceDate;
    public double totalFreightAmt;
    public double totalTaxAmt;
    public double totalInvoiceAmt;
    public String pricingCurrencyCode;
    public String currencyCodeDesignation;
    public String currencyCodeName;
    public double exchangeRate;
    public String billtoName;
    public String billtoAddr1;
    public String billtoAddr2;
    public String billtoCity;
    public String billtoState;
    public String billtoZip;
    public String billtoCounty;
    public String shiptoName;
    public String shiptoAddr1;
    public String shiptoAddr2;
    public String shiptoCity;
    public String shiptoState;
    public String shiptoZip;
    public String shipVia;
    public String rDCLoc;
    public String shawInd;
    public double taxPercent;

}
