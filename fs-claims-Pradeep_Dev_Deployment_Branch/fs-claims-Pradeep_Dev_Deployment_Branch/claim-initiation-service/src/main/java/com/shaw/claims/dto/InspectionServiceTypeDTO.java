package com.shaw.claims.dto;

import lombok.Data;


@Data
public class InspectionServiceTypeDTO {

	private Integer inspectionServiceTypeId;
	
	private String inspectionServiceTypeCode;
	
	private String inspectionServiceTypeName;
	
	private Integer displaySequence;
	
	private Integer statusId;
}
