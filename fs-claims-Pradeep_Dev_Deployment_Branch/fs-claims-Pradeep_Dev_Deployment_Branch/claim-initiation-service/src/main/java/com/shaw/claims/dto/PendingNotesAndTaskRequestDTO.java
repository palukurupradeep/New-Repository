package com.shaw.claims.dto;

import lombok.Data;

@Data
public class PendingNotesAndTaskRequestDTO extends BaseEntityDTO{
    private int noteTypeId;
    private int noteGroupId;
    private int claimId;
    private int noteTemplateId;
    private String lookupCode;
    private String claimNoteText;
}
