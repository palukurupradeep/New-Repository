package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InspectionRequestTypeResponseDTO {
    private int inspectionRequestTypeId;
    private String inspectionRequestTypeCode;
    private String inspectionRequestTypeName;
    private int displaySequence;
    private int statusId;
}
