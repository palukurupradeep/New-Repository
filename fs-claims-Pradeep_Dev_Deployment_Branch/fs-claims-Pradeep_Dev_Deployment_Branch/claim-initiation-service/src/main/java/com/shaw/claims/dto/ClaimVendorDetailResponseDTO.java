package com.shaw.claims.dto;

import com.shaw.claims.model.VendorAddress;
import com.shaw.claims.model.VendorService;
import com.shaw.claims.model.VendorServiceType;
import lombok.Data;

import java.util.List;

@Data
public class ClaimVendorDetailResponseDTO {
    private String claimVendorNumber;
    private Boolean isStatus;
    private int claimVendorId;
    private String psVendorLocation;
    private String legalName;
    private String dbaName;
    private String primaryContact;
    private String secondaryContact;
    private int phoneDialCodeId;
    private String phoneNumber;
    private String phoneExtension;
    private int cellDialCodeId;
    private String cellNumber;
    private String emailAddress;
    private List<ClaimVendorAddressResponseDTO> claimVendorAddressResponseDTOS;
    private List<ClaimVendorCertificateResponseDTO> claimVendorCertificateResponseDTOS;
    private List<ClaimVendorServiceTypeResponseDTO> claimVendorServiceTypeResponseDTOS;
    private List<ClaimVendorServiceResponseDTO> claimVendorServiceResponseDTOS;
}
