package com.shaw.claims.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.shaw.claims.model.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UpdateClaimStatusResponseDTO extends BaseEntity{

	private Integer claimId;

	private Integer statusId;

	private String statusName;
	
	private Integer workStatusId;
	
	private String workStatusName;
	
	private String workStatusGroupDescription;
	
	private LocalDateTime traceDate;
	
	private Integer oldClaimStatus;
	
	private String oldClaimStatusName;
	
	private Integer oldWorkStatus;
	
	private String oldWorkStatusName;
}
