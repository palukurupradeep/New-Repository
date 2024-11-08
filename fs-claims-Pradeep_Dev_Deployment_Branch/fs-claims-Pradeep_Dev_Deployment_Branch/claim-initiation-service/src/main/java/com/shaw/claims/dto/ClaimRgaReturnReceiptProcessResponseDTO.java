package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ClaimRgaReturnReceiptProcessResponseDTO {
    private String rgaNumber;
    private LocalDateTime date;
    private String status;
    private BigDecimal openQuantity;
    private BigDecimal receivedQuantity;
}
