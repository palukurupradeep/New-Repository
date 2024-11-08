package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetInvoiceDetailsResponse {
    public String invoiceNbr;
    public String invoiceDate;
    public String lineNbr;
    public String seqNbr;
    public String rollNbr;
    public String sellingStyleNbr;
    public String inventoryStyleNbr;
    public String colorNbr;
    public String sellingColorName;
    public String dyeLot;
    public String rcsCode;
    public String grade;
    public double widthFeet;
    public double widthInch;
    public double lengthFeet;
    public double lengthInch;
    public String unitofMeasure;
    public double quantity;
    public double unitPrice;
    public double netAmount;
    public String selco;
    public String region;
    public String district;
    public String territory;
    public String masterBolNbr;
    public String productCode;
    public String inventoryColorNbr;
    public String vendorId;
    public double convFactor;
}
