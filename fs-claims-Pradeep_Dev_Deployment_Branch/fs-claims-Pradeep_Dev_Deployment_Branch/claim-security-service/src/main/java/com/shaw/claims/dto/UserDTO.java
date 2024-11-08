package com.shaw.claims.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserDTO {
    private String userId;
    private String phoneNumber="";
    private String faxNumber="";
    private String emailAddress="";
    private Double returnAuthorizationLimit;
    private Double creditMemoLimit;
    private Double inspectionServiceLimit;
    private Double installationServiceLimit;
    private Double otherChargesLimit;
}
