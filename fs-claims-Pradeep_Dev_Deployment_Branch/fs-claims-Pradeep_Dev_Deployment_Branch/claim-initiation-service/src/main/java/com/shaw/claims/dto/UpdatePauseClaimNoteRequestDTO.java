package com.shaw.claims.dto;

import lombok.Data;

@Data
public class UpdatePauseClaimNoteRequestDTO {
	
	private int claimId;
	
	private Integer userId;
	
	private String noteText;
	
	private String traceDate;

}
