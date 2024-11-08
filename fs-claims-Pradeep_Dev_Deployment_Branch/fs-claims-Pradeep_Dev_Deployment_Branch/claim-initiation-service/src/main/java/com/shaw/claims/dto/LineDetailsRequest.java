package com.shaw.claims.dto;

import lombok.Data;

@Data
public class LineDetailsRequest {
	private Integer lineId;
	private String reasonCode;
}
