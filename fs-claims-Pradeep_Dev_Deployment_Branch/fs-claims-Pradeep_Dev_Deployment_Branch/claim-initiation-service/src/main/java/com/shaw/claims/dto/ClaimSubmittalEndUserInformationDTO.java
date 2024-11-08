package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimSubmittalEndUserInformationDTO {
    private String firstName;
    private String lastName;
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String businessPhoneNumber;
    private String homePhoneNumber;
    private String cellPhoneNumber;
    private char middleInitial;
    private String countryCode;
    private String city;
    private String stateCode;
    private String postalCode;
    private String county;
    private String businessPhoneDialCode;
    private String businessPhoneExtension;
    private String homePhoneDialCode;
    private String homePhoneExtension;
    private String cellPhoneDialCode;
    private String cellPhoneExtension;
    private String emailAddress;
}
