package com.shaw.claims.dto;

import lombok.Data;

@Data
public class MergedUnMergedResponseDTO {
    private int claimId;
    private String claimNumber;
    private String claimReasonCode;
    private int parentClaimId;
}
