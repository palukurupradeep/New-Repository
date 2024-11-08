package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class ClaimAssignmentDataResponseDTO {

    @JsonProperty("claimsArea")
    private String claimsArea;

    @JsonProperty("businessSegment")
    private String businessSegment;

    @JsonProperty("businessSegmentName")
    private String businessSegmentName;

    @JsonProperty("alignedAccount")
    private String alignedAccount;

    @JsonProperty("customerReferenceNumber")
    private String customerReferenceNumber;

    @JsonProperty("ccaAccount")
    private String ccaAccount;
    
    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("homeCenter")
    public String homeCenter;
}