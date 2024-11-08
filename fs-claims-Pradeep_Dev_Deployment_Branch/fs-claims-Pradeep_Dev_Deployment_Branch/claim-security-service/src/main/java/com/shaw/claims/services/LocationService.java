package com.shaw.claims.services;

import java.util.List;

import com.shaw.claims.dto.LocationDTO;

public interface LocationService {

	List<LocationDTO> fetchLocationsByLocationCode(String locationCode);

}
