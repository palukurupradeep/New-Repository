package com.shaw.claims.services;

import com.shaw.claims.model.Lookup;

import java.util.List;

public interface LookupService {
    public List<Lookup> getLookupTypeByCode(String lookupTypeCode);
}
