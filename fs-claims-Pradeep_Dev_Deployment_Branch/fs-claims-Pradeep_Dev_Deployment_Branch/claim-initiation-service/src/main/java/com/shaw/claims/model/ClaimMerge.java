package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "claimmerge", schema = "clm")
@Data
public class ClaimMerge extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimmergeid")
    private int claimMergeId;

    @Column(name = "parentclaimid")
    private int parentClaimId;

    @Column(name = "mergedclaimid")
    private int mergedClaimId;

    @Column(name = "primarydeleteflag")
    private boolean primaryDeleteFlag;

    @Column(name = "secondarydeleteflag")
    private boolean secondaryDeleteFlag;

    @Column(name = "primaryarchiveflag")
    private boolean primaryArchiveFlag;

    @Column(name = "secondaryarchiveflag")
    private boolean secondaryArchiveFlag;

    @Column(name = "statusid")
    private int statusId;
}