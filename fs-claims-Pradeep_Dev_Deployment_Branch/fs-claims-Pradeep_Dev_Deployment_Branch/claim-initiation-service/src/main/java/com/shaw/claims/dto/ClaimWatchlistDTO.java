package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimWatchlistDTO {
    private int claimId;
    private boolean addtoWatchList;
    private int userId;
}
