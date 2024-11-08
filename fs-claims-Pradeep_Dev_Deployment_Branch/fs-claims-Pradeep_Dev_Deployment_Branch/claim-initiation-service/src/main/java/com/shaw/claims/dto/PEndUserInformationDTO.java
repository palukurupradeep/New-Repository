package com.shaw.claims.dto;

import lombok.Data;

@Data
public class PEndUserInformationDTO extends BaseEntityDTO{
    private String firstName;
    private String lastName;
    private String companyName;
    private String addressLine1;
    private String addressLine2="";
    private String businessPhoneNumber;
    private String homePhoneNumber="";
    private String cellPhoneNumber="";
}
