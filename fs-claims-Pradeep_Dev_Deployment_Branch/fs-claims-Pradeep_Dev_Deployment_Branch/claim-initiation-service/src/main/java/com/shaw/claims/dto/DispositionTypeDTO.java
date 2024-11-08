package com.shaw.claims.dto;

import lombok.Data;

@Data
public class DispositionTypeDTO {
	private int dispositionTypeId;

	private String dispositionTypeCode;

	private String dispositionTypeDescription;

	private String pattern;

	private int displaySequence;

	private int statusId;
}
