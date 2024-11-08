package com.shaw.claims.services;

import com.shaw.claims.model.ContactDetails;

import java.util.List;

public interface ContactDetailsService {
    public List<ContactDetails> getContactDetails(String customerNumber, String ContactType);
}
