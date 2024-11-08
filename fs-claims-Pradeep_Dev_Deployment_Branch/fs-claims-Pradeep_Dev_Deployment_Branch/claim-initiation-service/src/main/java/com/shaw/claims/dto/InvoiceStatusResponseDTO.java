package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InvoiceStatusResponseDTO {

    private Integer invoiceStatusId;

    private String invoiceStatusCode;

    private String invoiceStatusDescription;

    private Integer displaySequence;

    private Integer statusId;
}