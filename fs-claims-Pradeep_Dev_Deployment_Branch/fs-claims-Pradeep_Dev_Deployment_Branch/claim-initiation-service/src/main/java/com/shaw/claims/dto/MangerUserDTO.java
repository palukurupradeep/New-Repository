package com.shaw.claims.dto;

import lombok.Data;

@Data
public class MangerUserDTO {
	private int userGroupId;
	private String userGroupName;
	private int managerId;
	private String managerName;
}
