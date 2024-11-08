package com.shaw.claims.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InStoreClaimNoteDTO {
    private LocalDateTime createdDateTime;
    private String claimNoteText;
}
