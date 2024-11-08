package com.shaw.claims.client;

import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ContactDetails;
import com.shaw.claims.source.response.ContactDetailsRoot;
import com.shaw.claims.source.response.FINServicesGetContactDetailsResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContactDetailsClient {

    Logger log = LogManager.getLogger(ContactDetailsClient.class);
    @Value("${rest.url}")
    private String restUrl;
    @Autowired
    RestTemplateClient restTemplateClient;
    public List<com.shaw.claims.model.ContactDetails> getContactDetails(String customerNumber, String contactType){
        log.info("Inside ContactDetailsClient.getContactDetails()");
        String formattedUrl = "";
        if (customerNumber!=null)
            formattedUrl += String.format("/getContactDetails?custNbr=%s",customerNumber);
        if (contactType!=null)
            formattedUrl += String.format("&contactType=%s",contactType);

        log.info("formattedUrl :: "+formattedUrl);
        RestTemplate restTemplate = restTemplateClient.buildRestTemplate();
        String url = restUrl + formattedUrl ;
        log.info("Final Url :: "+url);
        ContactDetailsRoot root = restTemplate.getForObject(url , ContactDetailsRoot.class);
        List<FINServicesGetContactDetailsResult> rows = root.getContactDetailsResponse().getContactDetailsResult();
        if (rows == null || rows.isEmpty()) {
			throw new CommonException(String.valueOf(HttpStatus.NOT_FOUND),
					"Record Not Found for customer number :: " + customerNumber);
		}
        return mergeContactDetails(rows);
    }

    private List<com.shaw.claims.model.ContactDetails> mergeContactDetails(List<FINServicesGetContactDetailsResult> rows) {
        log.info("Inside ContactDetailsClient.mergeContactDetails()");
        List<com.shaw.claims.model.ContactDetails> contactDetailsList = new ArrayList<>();
        rows.forEach(row ->{
            ContactDetails contactDetails = new ContactDetails();
            contactDetails.setCustomerNumber(row.getCustomerNumber());
            contactDetails.setContactType(row.getContactType());
            contactDetails.setContactTitle(row.getContactTitle());
            contactDetails.setContactTitleCode(row.getContactTitleCode());
            contactDetails.setContactCompanyName(row.getContactCompanyName());
            contactDetails.setContactPrimaryInd(row.getContactPrimaryInd());
            contactDetails.setFirstName(row.getFirstName());
            contactDetails.setMiddleName(row.getMiddleName());
            contactDetails.setLastName(row.getLastName());
            contactDetails.setSuffix(row.getSuffix());
            contactDetails.setNickName(row.getNickName());
            contactDetails.setContactComments(row.getContactComments());
            contactDetails.setCategory(row.getCategory());
            contactDetails.setPhonePrimaryInd(row.getPhonePrimaryInd());
            contactDetails.setPhoneType(row.getPhoneType());
            contactDetails.setCountryCode(row.getCountryCode());
            contactDetails.setAreaCode(row.getAreaCode());
            //contactDetails.setPhoneNumber(row.getPhoneNumber());
            
            if(row.getPhoneNumber() != null && !row.getPhoneNumber().isBlank() && row.getPhoneNumber().contains("-") && 
            		row.getAreaCode() != null && !row.getAreaCode().isBlank()) {
            	contactDetails.setPhoneNumber("("+row.getAreaCode()+")"+row.getPhoneNumber());
            } else {
            	contactDetails.setPhoneNumber(String.format("(%s)%s-%s",
            			row.getPhoneNumber().substring(0, 3), 
            			row.getPhoneNumber().substring(3, 6), 
            			row.getPhoneNumber().substring(6)));
            }
            contactDetails.setExtension(row.getExtension());
            contactDetails.setEmailPrimaryInd(row.getEmailPrimaryInd());
            contactDetails.setEmail(row.getEmail());
            contactDetails.setAddressPrimaryInd(row.getAddressPrimaryInd());
            contactDetails.setAddressType(row.getAddressType());
            contactDetails.setAddressLine1(row.getAddressLine1());
            contactDetails.setAddressLine2(row.getAddressLine2());
            contactDetails.setAddressLine3(row.getAddressLine3());
            contactDetails.setCity(row.getCity());
            contactDetails.setStateProvince(row.getStateProvince());
            contactDetails.setZipCode(row.getZipCode());
            contactDetails.setCounty(row.getCounty());
            contactDetails.setCountry(row.getCountry());
            contactDetails.setLatitude(row.getLatitude());
            contactDetails.setLongitude(row.getLongitude());
            contactDetailsList.add(contactDetails);
        });
        return contactDetailsList;
    }
}
