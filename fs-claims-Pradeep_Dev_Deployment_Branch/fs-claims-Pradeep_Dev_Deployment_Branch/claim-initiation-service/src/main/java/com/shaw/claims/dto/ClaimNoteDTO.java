package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class ClaimNoteDTO extends BaseEntityDTO {
	 
	    private int claimNoteId;

	    private NoteGroupDTO noteGroup;
	    
	    private NoteTemplateDTO noteTemplate;
	    
	    private NoteTypeDTO noteType;

	    private String claimNoteText;
	    
	    private boolean callLog;
	    
	    private String confidential;
	    
	    private String createdUserName;
	    
	    private String createdUserTitle;
	    
	    private String label;
	    
	    private String statusCode;

	    private TraceTaskDTO traceTask;
	    
	    private String noteTypeCode;

}
