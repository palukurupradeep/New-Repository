package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "claimarea", schema = "clm")
@Data
public class ClaimArea extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimareaid")
    private Integer claimAreaId;

    @Column(name = "claimareacode")
    private String claimAreaCode;

    @Column(name = "claimareadescription")
    private String claimAreaDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}
