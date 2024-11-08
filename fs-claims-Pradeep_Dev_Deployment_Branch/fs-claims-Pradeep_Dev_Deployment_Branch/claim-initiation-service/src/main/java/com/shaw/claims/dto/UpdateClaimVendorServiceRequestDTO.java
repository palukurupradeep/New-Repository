package com.shaw.claims.dto;

import lombok.Data;

@Data
public class UpdateClaimVendorServiceRequestDTO {
    private int vendorServiceId;
    private String inspectionServiceCode;
}
