package com.shaw.claims.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NumbersDTO {
    private String numberType; // Home, Fax, Business
    private int countryCode;
    private int extension;
    private int telephoneNumber;
}

