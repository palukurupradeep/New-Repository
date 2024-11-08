package com.shaw.claims.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CrmIssueResponseDTO {
private String crmNumber;
private int crmHeaderId;
private boolean managerApprovalFlag;
private BigDecimal amountUsd;
}
