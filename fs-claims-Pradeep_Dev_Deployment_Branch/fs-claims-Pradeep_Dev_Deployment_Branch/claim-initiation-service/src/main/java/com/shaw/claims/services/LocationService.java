package com.shaw.claims.services;

import com.shaw.claims.dto.LocationResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LocationService {
    List<LocationResponseDTO> location();
}
