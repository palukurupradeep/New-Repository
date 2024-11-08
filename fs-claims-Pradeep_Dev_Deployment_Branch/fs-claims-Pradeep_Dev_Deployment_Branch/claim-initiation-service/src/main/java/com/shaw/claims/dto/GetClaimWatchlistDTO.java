package com.shaw.claims.dto;

import lombok.Data;

@Data
public class GetClaimWatchlistDTO {
	private Integer claimId;
    private String customerNumber;
    private String claimNumber;
    private String workStatus;
    private Integer days;
    private Long count;
    private String customerName;
   
}
