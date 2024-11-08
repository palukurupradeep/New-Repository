package com.shaw.claims.dto;

import lombok.Data;

@Data
public class UpdateClaimVendorServiceTypeRequestDTO {
    private int vendorServiceTypeId;
    private String inspectionServiceTypeCodes;
}
