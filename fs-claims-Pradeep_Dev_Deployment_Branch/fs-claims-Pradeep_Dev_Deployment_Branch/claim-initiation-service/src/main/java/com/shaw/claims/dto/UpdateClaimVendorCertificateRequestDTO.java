package com.shaw.claims.dto;

import lombok.Data;

@Data
public class UpdateClaimVendorCertificateRequestDTO {
    private int vendorCertificateId;
    private String certifyingAgencyCode;
    private String serviceTypeCode;
    private String certificateNumber;
    private String expiryDate;
}
