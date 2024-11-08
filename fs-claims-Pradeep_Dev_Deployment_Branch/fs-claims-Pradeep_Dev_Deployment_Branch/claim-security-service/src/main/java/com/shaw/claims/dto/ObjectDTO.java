package com.shaw.claims.dto;

import lombok.Data;

import java.util.List;

@Data
public class ObjectDTO {
    private Integer objectId;
    private List<Integer> functionIds;
    private List<Integer> actionIds;
}
