package com.shaw.claims.services;

import com.shaw.claims.dto.LocationResponseDTO;
import com.shaw.claims.model.Locations;
import com.shaw.claims.repo.LocationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{

    Logger log = LogManager.getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationRepository locationRepository;
    @Override
    public List<LocationResponseDTO> location() {
        log.info("LocationServiceImpl.location");
        List<Locations> locationsList = locationRepository.findAllByStatus();
        List<LocationResponseDTO> locationResponseDTOList = locationsList.stream()
                .map(locations -> {
                    LocationResponseDTO locationResponseDTO = new LocationResponseDTO();
                    BeanUtils.copyProperties(locations,locationResponseDTO);
                    return locationResponseDTO;
                }).toList();
        return locationResponseDTOList;
    }
}
