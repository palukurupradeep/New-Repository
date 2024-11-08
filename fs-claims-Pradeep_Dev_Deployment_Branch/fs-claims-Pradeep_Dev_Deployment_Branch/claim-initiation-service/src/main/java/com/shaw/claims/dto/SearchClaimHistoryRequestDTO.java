package com.shaw.claims.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SearchClaimHistoryRequestDTO{
	public String primarySearchType;
	public String primaryValue;
	public String secondarySearchType;
	public String secondaryValue;
	public String claimStatus;
	public String claimInitiationFromDate;
	public String claimInitiationToDate;
	public String claimAmountFrom;
	public String claimAmountTo;
	public SearchHistoryEndUserDTO endUserInformation;
	public UserGroupMappingResponseDTO claimUser;
	public boolean isPrimarySearchToggle;

}