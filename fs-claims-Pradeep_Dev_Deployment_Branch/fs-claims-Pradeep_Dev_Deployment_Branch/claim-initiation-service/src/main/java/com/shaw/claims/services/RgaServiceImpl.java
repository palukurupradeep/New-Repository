package com.shaw.claims.services;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shaw.claims.dto.LocationDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ClaimAddress;
import com.shaw.claims.repo.ClaimAddressRepository;

@Service
public class RgaServiceImpl implements RGaServices {
	Logger log = LogManager.getLogger(RGaServices.class);
	@Autowired
	ClaimAddressRepository claimAddressRepository;
	
	@Value(("${claim-security.rest.url}"))
	private String restSecurityUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public List<String> fetchRecipients(String claimId) {
		log.info("Inside RgaServiceImpl.fetchRecipients claim Id :: {}",claimId);
		List<ClaimAddress> claimAddressesList = claimAddressRepository.findByClaim_ClaimId(claimId);
		if (claimAddressesList == null || claimAddressesList.isEmpty())
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Record Not Found with " + claimId);
		return claimAddressesList.stream().filter(address -> !address.getEmailAddress().isBlank()).map(claimAddress -> claimAddress.getEmailAddress())
				.collect(Collectors.toList());
	}

	@Override
	public List<LocationDTO> getReturnLocations(String locationCode) {
		return getLocations(locationCode);
	}
	
	private List<LocationDTO> getLocations(String locationCode) {
		String url = "/location/v1/fetchLocationsByLocationCode?locationCode=" + locationCode;
		String finalUrl = restSecurityUrl + url;
		log.info("Final Security Url :: " + finalUrl);
		ResponseEntity<List<LocationDTO>> responseEntity = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<LocationDTO>>() {
		});
		List<LocationDTO> response = responseEntity.getBody();
		if (response == null) {
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Record Not Found with location code :: " + locationCode);
		}
		return response;
	}


}
