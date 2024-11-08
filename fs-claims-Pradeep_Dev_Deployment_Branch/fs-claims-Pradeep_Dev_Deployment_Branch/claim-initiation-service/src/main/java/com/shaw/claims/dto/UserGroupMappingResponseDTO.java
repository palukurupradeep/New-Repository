package com.shaw.claims.dto;

import lombok.Data;

@Data
public class UserGroupMappingResponseDTO {

    private int userId;

    private int userGroupId;

    private int managerId;
}
