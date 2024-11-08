package com.shaw.claims.dto;

import lombok.Data;

@Data
public class EndUserInformationDTO extends PEndUserInformationDTO{
    private int endUserInformationId;
    private String firstName;
    private String lastName;
    private String companyName;
    private String addressLine1;
    private String addressLine2;
    private String businessPhoneNumber;
    private String homePhoneNumber;
    private String cellPhoneNumber;
    private char middleInitial;
    private int countryId;
    private String city;
    private int stateId;
    private String postalCode;
    private String county ="";
    private int businessPhoneDialCodeId;
    private String businessPhoneExtension;
    private int homePhoneDialCodeId;
    private String homePhoneExtension;
    private int cellPhoneDialCodeId;
    private String cellPhoneExtension;
    private String emailAddress = " ";
}
