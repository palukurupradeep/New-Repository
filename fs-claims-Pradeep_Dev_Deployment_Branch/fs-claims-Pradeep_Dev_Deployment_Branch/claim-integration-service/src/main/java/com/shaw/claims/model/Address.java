package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class Address {
	public String addressType;
	public String addressLine1;
	public String addressLine2;
	public String addressLine3;
	public String city;
	public String stateCode;
	public String zipCode;
	public String country;
	public String countryCode;
	public String latitude;
	public String longitude;
}
