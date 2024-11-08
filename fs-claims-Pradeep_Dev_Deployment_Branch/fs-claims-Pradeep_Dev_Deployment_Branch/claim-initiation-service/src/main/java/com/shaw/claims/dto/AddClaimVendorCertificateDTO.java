package com.shaw.claims.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AddClaimVendorCertificateDTO {
    private String certifyingAgencyCode;
    private String serviceTypeCode;
    private String certificateNumber;
    private String expiryDate;
}