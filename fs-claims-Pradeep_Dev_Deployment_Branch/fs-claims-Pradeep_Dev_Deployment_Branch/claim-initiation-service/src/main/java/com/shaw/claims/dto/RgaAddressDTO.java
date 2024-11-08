package com.shaw.claims.dto;

import lombok.Data;

@Data
public class RgaAddressDTO {
	private Integer rgaAddressId;
	private String addressTypeCode;
	private String fullName;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private int stateId;
	private int countryId;
	private String postalCode;
	private String stateName="";
	private String countryName="";
	

}
