package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimUpdateAddressDTO {
    private int claimAddressId;
    private int addressTypeId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
}
