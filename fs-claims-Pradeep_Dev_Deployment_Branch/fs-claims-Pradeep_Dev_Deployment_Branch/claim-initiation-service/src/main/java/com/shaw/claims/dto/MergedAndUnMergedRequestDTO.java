package com.shaw.claims.dto;

import lombok.Data;

@Data
public class MergedAndUnMergedRequestDTO {
    private int parentClaimId;
    private int mergedClaimId;
    private int unMergedClaimId;
    private int loggedInUserId;
    private String comment;
}
