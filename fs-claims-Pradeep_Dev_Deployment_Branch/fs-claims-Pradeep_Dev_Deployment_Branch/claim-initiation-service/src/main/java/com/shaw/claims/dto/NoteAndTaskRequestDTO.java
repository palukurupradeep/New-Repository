package com.shaw.claims.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteAndTaskRequestDTO extends BaseEntityDTO{
    private int claimNoteId;
    private Integer noteTypeId;
    private int noteGroupId;
    private int claimId;
    private int noteTemplateId;
    private String lookupCode;
    private String claimNoteText;
    private int traceTaskId;
    private Integer traceTypeId;
    private String traceReference;
    private String traceActionLink;
    private LocalDateTime traceDate;
    private boolean callLog;
    private int taskAssignedUserId;

}
