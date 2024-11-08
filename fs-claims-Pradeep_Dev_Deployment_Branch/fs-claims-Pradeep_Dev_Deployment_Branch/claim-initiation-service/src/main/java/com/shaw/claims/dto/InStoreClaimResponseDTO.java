package com.shaw.claims.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class InStoreClaimResponseDTO {
    public String accountNumber;
    public String accountName;
    public String claimNumber;
    public String claimStatus;
    public String territory;
    public String territoryManagerName;
    public String deduction;
    public LocalDateTime deductionDate;
    public BigDecimal deductionAmount;
    public String check;
    public LocalDateTime checkDate;
    public BigDecimal checkAmount;
    public BigDecimal priceClaimed;
    public String claimReason;
    public String claimResolution;
    public String submittedBy;
    public String submittedEmailId;
    public List<InStoreClaimDetailsResponseDTO> inStoreClaimDetailsResponseDTOS;
}
