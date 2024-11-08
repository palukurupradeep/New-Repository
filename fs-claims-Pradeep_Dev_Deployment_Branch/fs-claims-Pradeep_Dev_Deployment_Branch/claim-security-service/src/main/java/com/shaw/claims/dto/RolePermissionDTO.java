package com.shaw.claims.dto;

import java.util.List;

import lombok.Data;
@Data
public class RolePermissionDTO {
	private int roleId;
	private List<Integer> permissionIdData;
	private boolean isActive;

}
