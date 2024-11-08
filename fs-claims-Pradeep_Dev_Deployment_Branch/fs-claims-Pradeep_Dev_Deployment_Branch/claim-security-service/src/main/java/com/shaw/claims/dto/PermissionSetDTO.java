package com.shaw.claims.dto;

import lombok.Data;

import java.util.List;

@Data
public class PermissionSetDTO {
    private int permissionId;
    private List<ObjectDTO> objects;
}
