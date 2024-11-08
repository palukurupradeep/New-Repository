package com.shaw.claims.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class TraceTaskDTO {

    private int traceTaskId;

    private int assignedUserId;
    
    private String traceTaskType;
    
    private TraceTypeDTO traceType;
    
    private int createdByUserId;
    
    private LocalDateTime traceDate;
    
    private String createdUserName;
    
    private String assignedUserName;
    
    private String label;

}
