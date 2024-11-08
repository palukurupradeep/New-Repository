package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimRoutedDTO {

	private Integer claimId;
	private String routedUserName;
	private String loginUserName;
	private String note;
	private String userGroupCode;
	private Integer userId;
	private String[] toRecipients;
	private String[] ccRecipients;
	private Integer routedUserId;
}
