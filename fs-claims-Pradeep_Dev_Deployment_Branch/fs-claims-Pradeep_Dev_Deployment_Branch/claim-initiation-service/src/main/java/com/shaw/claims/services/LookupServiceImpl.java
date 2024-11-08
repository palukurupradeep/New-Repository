package com.shaw.claims.services;

import com.shaw.claims.model.Lookup;
import com.shaw.claims.repo.LookupRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LookupServiceImpl implements LookupService{
    Logger log = LogManager.getLogger(LookupServiceImpl.class);
    @Autowired
    LookupRepository lookupRepository;
    @Override
    public List<Lookup> getLookupTypeByCode(String lookupTypeCode) {
        log.info("Inside LookupServiceImpl.getLookupTypeByCode");
        return lookupRepository.getLookupTypeByCode(lookupTypeCode);
    }
}
