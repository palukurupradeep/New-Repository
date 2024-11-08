package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClaimInitiationResponseDTO {
    private int claimId;
    private String claimNumber;
    private String customerNumber;
    private String claimCategoryCode;
    private String reasonCode;
    private BigDecimal claimAmount;
    private Boolean jobStopped;
    private Boolean priorityClaim;
}
