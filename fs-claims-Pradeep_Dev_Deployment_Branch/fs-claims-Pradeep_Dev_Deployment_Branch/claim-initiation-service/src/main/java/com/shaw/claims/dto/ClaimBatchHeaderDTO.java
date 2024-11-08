package com.shaw.claims.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClaimBatchHeaderDTO {

    private int userGroupId;
    private String userGroupName;
    private int claimBatchHeaderId;
    private String batchDescription;
    private String batchStatusCode;
    private boolean customerRollUp;
    private boolean deliverCreditMemo;
    private boolean printBatch;
    private int createdByUserId;
    private int processedByUserId;
    private LocalDate createdDateTime;
    private LocalDate processedDateTime;
    private List<ClaimBatchDetailsDTO> claimBatchDetailsDTOS;
    private String createdUserName;
    private String processedUserName;
}
