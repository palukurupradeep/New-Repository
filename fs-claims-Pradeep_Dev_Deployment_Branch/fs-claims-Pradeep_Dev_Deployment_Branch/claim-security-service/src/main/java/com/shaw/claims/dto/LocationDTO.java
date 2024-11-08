package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class LocationDTO {
	private int locationId;
	private String locationCode;
	private String locationName;
	private String locationType;
	private boolean fullService;
	private boolean threePl;
	private String geoLocation;
	private double latitude;
	private double longitude;
	private String fullAddress;
	private int statusId;
}
