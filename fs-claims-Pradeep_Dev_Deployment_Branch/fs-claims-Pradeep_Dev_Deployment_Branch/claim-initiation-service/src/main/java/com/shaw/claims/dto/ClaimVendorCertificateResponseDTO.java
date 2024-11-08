package com.shaw.claims.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ClaimVendorCertificateResponseDTO {
    private String certifyingAgencyCode;
    private String inspectionServiceTypeCode;
    private String certificateNumber;
    private Date expiryDate;
}
