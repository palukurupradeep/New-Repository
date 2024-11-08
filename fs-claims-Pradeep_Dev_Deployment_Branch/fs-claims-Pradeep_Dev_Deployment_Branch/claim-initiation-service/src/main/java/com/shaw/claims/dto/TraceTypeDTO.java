package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TraceTypeDTO extends BaseEntityDTO{

	private Integer traceTypeId;

	private String traceTypeCode;

	private String traceTypeDescription;

	private int traceTypeDays;
	
}
