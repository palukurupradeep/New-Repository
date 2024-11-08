package com.shaw.claims.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class InStoreClaimsResponseDTO {
    private String claimNumber;
    private LocalDateTime claimDate;
    private Date dueDate;
    private String accountNumber;
    private String accountName;
    private String type;
    private String status;
    private String territory;
    private String submitterName;
}
