package com.shaw.claims.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDetails {
    private String customerNumber;
    private String contactDisposition;
    private String contactType;
    private String contactTitleCode;
    private String contactTitle;
    private String contactCompanyName;
    private String contactPrimaryInd;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String nickName;
    private String contactComments;
    private String category;
    private String phonePrimaryInd;
    private String phoneType;
    private String countryCode;
    private String areaCode;
    private String phoneNumber;
    private String extension;
    private String emailPrimaryInd;
    private String email;
    private String addressPrimaryInd;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String city;
    private String stateProvince;
    private String zipCode;
    private String country;
    private String county;
    private String latitude;
    private String longitude;
}
