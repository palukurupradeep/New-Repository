package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimBuilderInstalledFlooringDataDTO {

    private String installDate;
    private String complaintDate;
    private String installType;
    private String percentInstalled;
    private Boolean professionallyCleaned;
    private String cleaningMethod;
}
