package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimAddressDTO {

	private String addressTypeCode;
	private String addressType;
	private String firstName;
	private String lastName;
	private String fullName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String country;
	private String zipCode;
}
