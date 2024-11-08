package com.shaw.claims.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserRoleDTO {
    private int roleId;
    private List<Integer> userIds;
}
