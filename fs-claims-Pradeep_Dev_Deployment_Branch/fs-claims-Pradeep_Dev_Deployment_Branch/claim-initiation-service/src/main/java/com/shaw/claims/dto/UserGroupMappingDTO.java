package com.shaw.claims.dto;

import java.util.List;

import lombok.Data;
@Data
public class UserGroupMappingDTO {
	private int userId;
	private List<MangerUserDTO> userGroupDTO;
}
