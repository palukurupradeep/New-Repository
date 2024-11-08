package com.shaw.claims.services;

import com.shaw.claims.dto.InspectionRequestTypeResponseDTO;
import com.shaw.claims.dto.InspectionServicesResponseDTO;

import java.util.List;

public interface InspectionServices {
    List<InspectionServicesResponseDTO> getAllInspectionServices();

    List<InspectionRequestTypeResponseDTO> getAllActiveInspectionRequestTypes();
}
