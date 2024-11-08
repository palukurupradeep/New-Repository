package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class SmartCodeMessageDTO {
	String ruleCategory;
	String errorMessage;
	String successMessage;
	Boolean override;
	String autoCode;
	Integer claimId;
	String updatedReasonCode;
}
