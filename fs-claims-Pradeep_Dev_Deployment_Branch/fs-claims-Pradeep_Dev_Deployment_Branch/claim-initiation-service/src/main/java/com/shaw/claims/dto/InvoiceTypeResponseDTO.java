package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InvoiceTypeResponseDTO {
    private int invoiceTypeId;
    private String invoiceTypeCode;
    private String invoiceTypeDescription;
    private int displaySequence;
    private int statusId;
}