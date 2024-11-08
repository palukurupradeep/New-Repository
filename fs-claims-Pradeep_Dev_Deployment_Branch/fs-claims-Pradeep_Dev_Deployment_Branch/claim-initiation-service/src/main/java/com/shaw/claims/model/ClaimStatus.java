package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "claimstatus", schema = "clm")
public class ClaimStatus extends BaseEntity{
    @Id
    @Column(name = "claimstatusid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int claimStatusId;
    @Column(name = "claimstatuscode")
    private String claimStatusCode;
    @Column(name = "claimstatusdescription")
    private String claimStatusDescription;
    @Column(name = "displaysequence")
    private int displaySequence;
    @Column(name = "statusid")
    private int statusId;
}
