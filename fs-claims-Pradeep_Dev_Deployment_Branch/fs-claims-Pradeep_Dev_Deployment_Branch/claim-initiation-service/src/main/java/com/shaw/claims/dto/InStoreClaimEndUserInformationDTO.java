package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InStoreClaimEndUserInformationDTO {
    private String firstName;
    private String lastName;
    private String companyName;
    private String country;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private int businessPhoneDialCodeId;
    private String businessPhoneNumber;
    private String businessPhoneExtension;
}
