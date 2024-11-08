package com.shaw.claims.services;

import com.shaw.claims.client.ContactDetailsClient;
import com.shaw.claims.model.ContactDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContactDetailsServiceImpl implements ContactDetailsService{
    Logger log = LogManager.getLogger(ContactDetailsServiceImpl.class);
    @Autowired
    private ContactDetailsClient contactDetailsClient;
    public List<ContactDetails> getContactDetails(String customerNumber, String contactType){
        log.info("Inside ContactDetailsServiceImpl");
        return contactDetailsClient.getContactDetails(customerNumber, contactType);
    }
}
