package com.shaw.claims.util;

import com.shaw.claims.dto.GeoLocationDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinateParser {
    public static Map<Integer, GeoLocationDTO> parseCoordinatesToMap(List<GeoLocationDTO> coordinates) {
        Map<Integer, GeoLocationDTO> map = new HashMap<>();
        GeoLocationDTO latLng = null;
        int index = 0;
        for (GeoLocationDTO loc : coordinates) {
            latLng = new GeoLocationDTO(loc.getLatitude(), loc.getLongitude());
            map.put(index++, latLng);
        }
        index = 0;
        return map;
    }
}
