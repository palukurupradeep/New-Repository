package com.shaw.claims.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ADUserDTO {
    private String displayName;
    private String givenName;
    private String jobTitle;
    private String mail;
    private String mobilePhone;
    private String officeLocation;
    private String preferredLanguage;
    private String surname;
    private String userPrincipalName;
    private String id;
    private String department;
    private String country;
    private String city;
    private String employeeId;
    private String onPremisesSamAccountName;
}