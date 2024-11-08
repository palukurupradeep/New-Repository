package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InvoiceLineTypeResponseDTO {
    private int invoiceLineTypeId;
    private String invoiceLineTypeCode;
    private String invoiceLineTypeDescription;
    private int displaySequence;
    private int statusId;
}