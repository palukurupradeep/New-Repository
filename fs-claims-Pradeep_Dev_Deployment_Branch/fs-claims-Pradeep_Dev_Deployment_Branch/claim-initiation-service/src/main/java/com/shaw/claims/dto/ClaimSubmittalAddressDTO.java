package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ClaimSubmittalAddressDTO {
    private String addressTypeCode;
    private String firstName;
    private String lastName;
    private String fullName;
    private String formattedName;
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String phoneDialCode;
    private String phoneNumber;
    private String homePhoneNumber;
    private String phoneExtension;
    private String faxDialCode;
    private String faxNumber;
    private String faxExtension;
    private String cellPhoneDialCode;
    private String cellPhoneNumber;
    private String cellPhoneExtension;
    private String latitude;
    private String longitude;
    private BigDecimal mapXAxis;
    private BigDecimal mapYAxis;
    private BigDecimal mapZAxis;
    private String category;
    private String purpose;
    private String businessAreaCode;
    private String businessCountryCode;
    private String businessCategory;
    private String businessPurpose;
    private String faxNumberCategory;
    private String faxNumberPurpose;
    private String faxAreaCode;
    private String faxCountryCode;
    private String homeNumberCountryCode;
    private String homePhoneAreaCode;
    private String homePhoneCountryCode;
    private String homePhoneExtension;
    private String cellNumberAreaCode;
    private String cellNumberCountryCode;
    private String cellNumberCategory;
    private String cellNumberPurpose;
    private String emailCountryCode;
    private String emailCategory;
    private String emailPurpose;
}
