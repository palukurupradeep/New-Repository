package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class AddClaimVendorDTO extends BaseEntityDTO{
    private String federalId;
    private String psVendorId;
    private String psVendorLocation;
    private String legalName;
    private String dbaName;
    private String primaryContact;
    private String secondaryContact;
    private String w9ReceivedDate;
    private String psVendorFormDate;
    private String claimsProfileFormDate;
    private String profileReviewDate;
    private String backgroundCheckDate;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private int phoneDialCodeId;
    private String phoneNumber;
    private String phoneExtension;
    private int cellDialCodeId;
    private String cellNumber;
    private String emailAddress;
    private Boolean isStatus;
    private List<AddClaimVendorAddressDTO> addClaimVendorAddressDTOS;
    private List<AddClaimVendorCertificateDTO> addClaimVendorCertificateDTOS;
    private List<String> inspectionServiceCodes;
    private List<String> inspectionServiceTypeCodes;
}