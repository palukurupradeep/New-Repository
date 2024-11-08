package com.shaw.claims.util;

import com.shaw.claims.dto.ContactDetailsDTO;
import com.shaw.claims.dto.UserGroupMappingResponseDTO;
import com.shaw.claims.exception.CommonException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

@Component
public class TDVServiceUtil {
    Logger log = LogManager.getLogger(TDVServiceUtil.class);

    @Value(("${claim-security.rest.url}"))
    private String restSecurityUrl;
    @Value("${claim-integration.rest.url}")
    private String restUrl;
    @Autowired
    private RestTemplate restTemplate;


    public int getUserID(String activeDirectoryId) {
        int userId = 0;
        String url = "/users/v1/getUserIdByActiveDirectoryId?activeDirectoryId=" + activeDirectoryId;
        String finalUrl = restSecurityUrl + url;
        log.info("Final Security Url :: " + finalUrl);
        Integer response = restTemplate.getForObject(finalUrl, Integer.class);
        if (response != null) {
            userId = response;
        }
        return userId;
    }

    public Set<ContactDetailsDTO> getContactDetails(String customerNumber) {
        Set<ContactDetailsDTO> contactDetailsDTOSet = null;
        String url = "/contacts/v1/getContactDetails?customerNumber=" + customerNumber;
        String finalUrl = restUrl + url;
        log.info("Final Url :: " + finalUrl);
        ResponseEntity<Set<ContactDetailsDTO>> response = restTemplate.exchange(finalUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<Set<ContactDetailsDTO>>() {
                }
        );
        if (response.getStatusCode().is2xxSuccessful()) {
            contactDetailsDTOSet = response.getBody();
        } else {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with CustomerNumber :: " + customerNumber);
        }
        return contactDetailsDTOSet;
    }

    public List<UserGroupMappingResponseDTO> getUserGroupsMapping(int userId) {
        String url = "/users/v1/getUserGroupById?userId=" + userId;
        String finalUrl = restSecurityUrl + url;
        log.info("Final Security Url :: " + finalUrl);
        ResponseEntity<List<UserGroupMappingResponseDTO>> responseEntity = restTemplate.exchange(
                finalUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<UserGroupMappingResponseDTO>>() {
                });
        List<UserGroupMappingResponseDTO> response = responseEntity.getBody();
        if (response == null || response.isEmpty()) {
            throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
                    "Record Not Found with userId :: " + userId);
        }
        return response;
    }
}
