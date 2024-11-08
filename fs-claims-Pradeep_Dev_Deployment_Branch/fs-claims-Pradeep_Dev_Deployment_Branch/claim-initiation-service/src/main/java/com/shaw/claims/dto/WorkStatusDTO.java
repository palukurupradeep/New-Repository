package com.shaw.claims.dto;

import lombok.Data;

@Data
public class WorkStatusDTO {

	private Integer workStatusId;
	private String workStatusCode;
	private String workStatusDescription;
	private String workStatusName;
	private int displaySequence;
	private int statusId;
}
