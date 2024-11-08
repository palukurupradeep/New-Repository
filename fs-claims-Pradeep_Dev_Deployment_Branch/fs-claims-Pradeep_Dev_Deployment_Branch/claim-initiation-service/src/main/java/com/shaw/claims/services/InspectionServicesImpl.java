package com.shaw.claims.services;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.InspectionRequestTypeResponseDTO;
import com.shaw.claims.dto.InspectionServicesResponseDTO;
import com.shaw.claims.repo.InspectionRequestTypeRepository;
import com.shaw.claims.repo.InspectionServiceRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InspectionServicesImpl implements InspectionServices{

    Logger log = LogManager.getLogger(InspectionServicesImpl.class);

    @Autowired
    private InspectionServiceRepository inspectionServiceRepository;
    @Autowired
    private InspectionRequestTypeRepository inspectionRequestTypeRepository;

    @Override
    public List<InspectionServicesResponseDTO> getAllInspectionServices() {
        log.info("InspectionServiceImpl.getAllInspectionServices");
        return inspectionServiceRepository.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE).stream()
                .map(data -> {
                    InspectionServicesResponseDTO inspectionServicesResponseDTO = new InspectionServicesResponseDTO();
                    BeanUtils.copyProperties(data,inspectionServicesResponseDTO);
                    return inspectionServicesResponseDTO;
                }).collect(Collectors.toList());
    }

    @Override
    public List<InspectionRequestTypeResponseDTO> getAllActiveInspectionRequestTypes() {
        log.info("InspectionServiceImpl.getAllActiveInspectionRequestTypes");
        return inspectionRequestTypeRepository.findByStatusIdOrderByDisplaySequenceAsc(CommonConstant.ACTIVE)
                .stream().map(requestType -> {
                    InspectionRequestTypeResponseDTO inspectionRequestTypeResponseDTO = new InspectionRequestTypeResponseDTO();
                    BeanUtils.copyProperties(requestType,inspectionRequestTypeResponseDTO);
                    return inspectionRequestTypeResponseDTO;
                }).collect(Collectors.toList());
    }
}
