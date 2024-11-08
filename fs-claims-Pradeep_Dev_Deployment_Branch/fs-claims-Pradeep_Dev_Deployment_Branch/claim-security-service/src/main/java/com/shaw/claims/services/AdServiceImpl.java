package com.shaw.claims.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shaw.claims.dto.ADUserDTO;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.Users;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdServiceImpl implements AdService {
    Logger log = LogManager.getLogger(AdServiceImpl.class);

    @Value("${ad.token-uri}")
    private String tokenUri;
    @Value("${ad.graph-uri}")
    private String graphUri;
    @Value("${ad.grant-type}")
    private String grantType;
    @Value("${ad.client-id}")
    private String clientId;
    @Value("${ad.client-secret}")
    private String clientSecret;
    @Value("${ad.scope}")
    private String scope;
    @Value("${ad.graph-select}")
    private String graphSelect;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    UserService userService;
    private String getToken() {
        log.info("Inside AdServiceImpl.getToken");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> inputMap = new LinkedMultiValueMap<>();
        inputMap.add("grant_type", grantType);
        inputMap.add("client_id", clientId);
        String secret = clientSecret.substring(2, clientSecret.length() - 2);
        inputMap.add("client_secret", secret);
        inputMap.add("scope", scope);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(inputMap, headers);
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.postForEntity(tokenUri, entity, String.class);
        JsonElement jsonElement = JsonParser.parseString(response.getBody());
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String accessToken = jsonObject.get("access_token").getAsString();
        return accessToken;
    }
    public List<ADUserDTO> fetchUserFromAD(String firstName, String lastName, String activeDirId) throws Exception {
        log.info("Inside AdServiceImpl.fetchUserFromAD");
        String token = getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("ConsistencyLevel", "eventual");
        boolean flag = false;
        String finalUri = graphUri + "/users?$filter=";
        if (firstName != null){
            flag = true;
            finalUri = finalUri + "startswith(givenName,'"+firstName+"')";
        }
        if (lastName != null){
            if(firstName!=null)
                finalUri = finalUri + " and ";
            flag = true;
            finalUri = finalUri + "startswith(surname,'"+lastName+"')";
        }
        if (activeDirId != null){
            flag = true;
            finalUri = finalUri + "startswith(onPremisesSamAccountName,'"+activeDirId+"')&$count=true";
        }
        finalUri = finalUri + graphSelect;
        log.info(finalUri);
        List<ADUserDTO> dtoList = new ArrayList<>();
        if(flag) {
            String encodedUri = finalUri.replaceAll(" ", "%20");
            RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, new URI(encodedUri));
            ResponseEntity<Object> response = restTemplate.exchange(request, Object.class);
            ADUserDTO dto = null;
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> map = (Map<String, Object>) response.getBody();
                if (null != map.get("value")) {
                    List<Map<String, String>> list = (List<Map<String, String>>) map.get("value");
                    for (Map<String, String> m : list) {
                        dto = new ADUserDTO();
                        setUserDataFromAD(dto, m);
                        dtoList.add(dto);
                    }
                } else {
                    Map<String, String> m = (Map<String, String>) response.getBody();
                    dto = new ADUserDTO();
                    setUserDataFromAD(dto, m);
                    dtoList.add(dto);
                }
            } else {
                throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST), "Error while fetching user profile from Azure AD");
            }
        }
        return dtoList;
    }
    @Override
    public void syncUsersFromAD(Integer userId) throws Exception{
        Users user = null;
        List<Users> usersList = null;
        if(userId!=null) {
            user = userService.findUserById(userId);
            List<ADUserDTO> adUserList = fetchUserFromAD(null, null, user.getActiveDirectoryId());
            for (ADUserDTO adUser:adUserList) {
                updateUserDataFromAd(user, adUser);
                userService.addUser(user);
            }
        }
        else {
            usersList = userService.findAllUsers();
            for (Users usr:usersList) {
                List<ADUserDTO> adUserList = fetchUserFromAD(null, null, usr.getActiveDirectoryId());
                for (ADUserDTO adUser:adUserList) {
                    updateUserDataFromAd(usr, adUser);
                    userService.addUser(usr);
                }
            }
        }
    }
    private void setUserDataFromAD(ADUserDTO dto, Map<String, String> m) {
        dto.setId(m.get("id"));
        dto.setMail(m.get("mail"));
        dto.setDisplayName(m.get("displayName"));
        dto.setSurname(m.get("surname"));
        dto.setJobTitle(m.get("jobTitle"));
        dto.setGivenName(m.get("givenName"));
        dto.setMobilePhone(m.get("mobilePhone"));
        dto.setOfficeLocation(m.get("officeLocation"));
        dto.setPreferredLanguage(m.get("preferredLanguage"));
        dto.setUserPrincipalName(m.get("userPrincipalName"));
        dto.setDepartment(m.get("department"));
        dto.setCountry(m.get("country"));
        dto.setCity(m.get("city"));
        dto.setEmployeeId(m.get("employeeId"));
        dto.setOnPremisesSamAccountName(m.get("onPremisesSamAccountName"));
    }
    private void updateUserDataFromAd(Users user, ADUserDTO adUser){
        if(user.getActiveDirectoryId().equals(adUser.getOnPremisesSamAccountName())) {
            if (user.getUserName() == null || !user.getUserName().equals(adUser.getOnPremisesSamAccountName() != null ? adUser.getOnPremisesSamAccountName() : "")) {
                user.setUserName(adUser.getOnPremisesSamAccountName() != null ? adUser.getOnPremisesSamAccountName() : "");
            }
            if (user.getFirstName() == null || !user.getFirstName().equals(adUser.getGivenName() != null ? adUser.getGivenName() : "")) {
                user.setFirstName(adUser.getGivenName() != null ? adUser.getGivenName() : "");
            }
            if (user.getLastName() == null || !user.getLastName().equals(adUser.getSurname() != null ? adUser.getSurname() : "")) {
                user.setLastName(adUser.getSurname() != null ? adUser.getSurname() : "");
            }
            if (user.getEmailAddress() == null || !user.getEmailAddress().equals(adUser.getUserPrincipalName() != null ? adUser.getUserPrincipalName() : "")) {
                user.setEmailAddress(adUser.getUserPrincipalName() != null ? adUser.getUserPrincipalName() : "");
            }
            if (user.getTitle() == null || !user.getTitle().equals(adUser.getJobTitle() != null ? adUser.getJobTitle() : "")) {
                user.setTitle(adUser.getJobTitle() != null ? adUser.getJobTitle() : "");
            }
            if (user.getDepartmentName() == null || !user.getDepartmentName().equals(adUser.getDepartment() != null ? adUser.getDepartment() : "")) {
                user.setDepartmentName(adUser.getDepartment() != null ? adUser.getDepartment() : "");
            }
            if (user.getObjectId() == null || !user.getObjectId().equals(adUser.getId() != null ? adUser.getId() : "")) {
                user.setObjectId(adUser.getId() != null ? adUser.getId() : "");
            }
            user.setModifiedDateTime(LocalDateTime.now());
        }
    }
}
