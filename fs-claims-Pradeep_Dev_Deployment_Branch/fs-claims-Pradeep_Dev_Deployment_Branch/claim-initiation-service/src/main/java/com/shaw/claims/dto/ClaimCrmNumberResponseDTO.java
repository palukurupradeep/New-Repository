package com.shaw.claims.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class ClaimCrmNumberResponseDTO {
	private String crmNumber;
	
	private LocalDateTime crmDate;

}
