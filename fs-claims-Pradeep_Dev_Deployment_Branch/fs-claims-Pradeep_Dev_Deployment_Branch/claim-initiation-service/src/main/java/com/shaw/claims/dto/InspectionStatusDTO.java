package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InspectionStatusDTO {

	private Integer inspectionStatusId;
	
	private String inspectionStatusCode;
	
	private String inspectionStatusDescription;
	
	private Integer displaySequence;
	
	private Integer statusId;

}
