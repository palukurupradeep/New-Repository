package com.shaw.claims.dto;

import lombok.Data;

@Data
public class InstalledFlooringDataDTO {
    private String installDate;
    private String complaintDate;
    private String installType;
    private int percentInstalled;
    private boolean professionallyCleaned;
    private String cleaningMethod;
}
