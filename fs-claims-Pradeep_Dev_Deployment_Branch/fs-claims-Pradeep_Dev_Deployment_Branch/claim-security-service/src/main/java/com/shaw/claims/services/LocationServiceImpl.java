package com.shaw.claims.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shaw.claims.dto.LocationDTO;
import com.shaw.claims.model.Locations;
import com.shaw.claims.repo.LocationRepository;

@Service
public class LocationServiceImpl implements LocationService{
	
	@Autowired
	LocationRepository locationRepository;

	@Override
	public List<LocationDTO> fetchLocationsByLocationCode(String locationCode) {
		Optional<Locations> locationDetails = locationRepository.findByLocationCode(locationCode);
		List<LocationDTO> locationDTOList = null;
		if (locationDetails.isPresent()) {
			if (locationDetails.get().getLocationType().equalsIgnoreCase("SOFT")) {
				locationDTOList = getLocationsByLocationType("SOFT");
			} else if (locationDetails.get().getLocationType().equalsIgnoreCase("HARD")) {
				locationDTOList = getLocationsByLocationType("HARD");
			}
		}
		return locationDTOList;
	}

	private List<LocationDTO> getLocationsByLocationType(String locationType) {
		List<Locations> locationList = locationRepository.findByLocationType(locationType);
		List<LocationDTO> locationDTOList = null;
		if (locationList != null) {
			locationDTOList = locationList.stream().map(location -> {
				LocationDTO locationDTO = new LocationDTO();
				locationDTO.setLocationId(location.getLocationId());
				locationDTO.setLocationCode(location.getLocationCode());
				locationDTO.setLocationName(location.getLocationName());
				return locationDTO;
			}).collect(Collectors.toList());
		}
		return locationDTOList;
	}

}
