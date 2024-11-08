package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimNoteRequestDTO {
    private String loggedInUser;
    private String noteTypeCode;
    private String noteTemplateName;
    private String claimNumber;
    private String claimNoteText;
}
