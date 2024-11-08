package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimRouteRequestDTO extends BaseEntityDTO{
    public int claimId;
    public int userId;
    public String userGroupCode;

}
