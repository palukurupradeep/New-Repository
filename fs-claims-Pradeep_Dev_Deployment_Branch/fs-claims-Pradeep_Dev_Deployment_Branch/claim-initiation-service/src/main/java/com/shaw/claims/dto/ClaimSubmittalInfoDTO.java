package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ClaimSubmittalInfoDTO {
    private boolean sampleAvailable;
    private boolean flooringInstalled;
    private boolean labelMatch;
    private boolean visibleDamage;
    private boolean holesDamage;
    private boolean tearsDamage;
    private boolean poleDamage;
    private boolean wrinkleDamage;
    private boolean creaseDamage;
    private boolean dirtyDamage;
    private boolean telescopedDamage;
    private boolean wetDamage;
    private boolean trailerDamage;
    private boolean nailsDamage;
    private boolean ribsDamage;
    private boolean wallDamage;
    private boolean otherDamage;
    private boolean attachments;
    private String requestedCarrier;
    private String shippingCarrier;
    private NationalGlobalInfoDTO nationalGlobalInfo;
    private String stopJobFlag;
    private InstalledFlooringDataDTO installedFlooringData;
}
