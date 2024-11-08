package com.shaw.claims.services;

import com.shaw.claims.dto.GeoLocationDTO;

import java.util.List;

public interface AzureMapService {
    String getLocation(List<String> addrLst);
    String getCoordinates(String addrLst);
    void updateLocation(Integer locationId);
    GeoLocationDTO findClosestRDC(GeoLocationDTO origin);
}
