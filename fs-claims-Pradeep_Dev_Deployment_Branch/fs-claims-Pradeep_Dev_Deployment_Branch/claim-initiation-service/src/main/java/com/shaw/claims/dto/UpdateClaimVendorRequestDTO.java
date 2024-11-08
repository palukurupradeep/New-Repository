package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateClaimVendorRequestDTO extends BaseEntityDTO{
    private int claimVendorId;
    private String claimVendorNumber;
    private String legalName;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Boolean isStatus;
    private String w9ReceivedDate;
    private String psVendorFormDate;
    private String claimsProfileFormDate;
    private String profileReviewDate;
    private String backgroundCheckDate;
    private int phoneDialCodeId;
    private String phoneNumber;
    private String phoneExtension;
    private int cellDialCodeId;
    private String cellNumber;
    private String emailAddress;
    private List<UpdateClaimVendorCertificateRequestDTO> vendorCertificateRequestDTOS;
    private List<UpdateClaimVendorServiceRequestDTO> vendorServiceRequestDTOS;
    private List<UpdateClaimVendorServiceTypeRequestDTO> vendorServiceTypeRequestDTOS;
}
