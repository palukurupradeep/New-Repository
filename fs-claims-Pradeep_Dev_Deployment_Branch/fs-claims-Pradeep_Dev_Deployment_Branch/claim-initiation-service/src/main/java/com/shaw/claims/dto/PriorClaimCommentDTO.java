package com.shaw.claims.dto;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PriorClaimCommentDTO {
    private String priorType;
    private String priorInvoice;
    private String priorPhone;
    private String priorClaim;
    private String priorComments;
}

