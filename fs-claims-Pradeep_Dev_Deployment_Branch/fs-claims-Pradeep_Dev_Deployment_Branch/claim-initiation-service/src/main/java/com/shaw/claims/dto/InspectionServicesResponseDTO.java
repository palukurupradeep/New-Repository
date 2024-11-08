package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InspectionServicesResponseDTO {
    private Integer inspectionServiceId;

    private String inspectionServiceCode;

    private String inspectionServiceName;

    private Integer displaySequence;

    private Integer statusId;
}
