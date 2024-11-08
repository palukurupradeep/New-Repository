package com.shaw.claims.dto;

import com.shaw.claims.model.BaseEntity;

import lombok.Data;

@Data
public class UserApprovalLimitDTO  extends BaseEntity{

	private int userApprovalLimitId;
	private String userId;
	private String approvalLimitType;
	private double approvalLimit;
	private int statusId = 1;
}
