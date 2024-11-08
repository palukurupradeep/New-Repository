package com.shaw.claims.dto;

import lombok.Data;

@Data
public class AddClaimVendorAddressDTO {
    private String addressTypeCode;
    private String fullName;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String stateCode;
    private String countryCode;
    private String county;
    private String postalCode;

}