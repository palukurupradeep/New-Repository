package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.LocationDTO;

public interface RGaServices {

	public List<String> fetchRecipients(String claimId);

	public List<LocationDTO> getReturnLocations(String locationCode);

}
