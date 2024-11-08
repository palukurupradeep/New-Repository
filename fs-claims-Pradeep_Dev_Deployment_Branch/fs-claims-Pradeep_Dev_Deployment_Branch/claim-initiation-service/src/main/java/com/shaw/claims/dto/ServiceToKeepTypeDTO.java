package com.shaw.claims.dto;


import lombok.Data;

@Data
public class ServiceToKeepTypeDTO extends BaseEntityDTO{
	
	
	private Integer serviceToKeepTypeId;
	
	private String serviceToKeepTypeCode;	
	
	private String serviceToKeepTypeDescription;
	
	private Integer displaySequence;
	
	private Integer statusId;
	

}
