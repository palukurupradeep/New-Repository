package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "claimuserarea", schema = "clm")
@Data
public class ClaimUserArea extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimuserareaid")
    private Integer claimUserAreaId;

    @Column(name = "userid")
    private int userId;

    @Column(name = "claimareaid")
    private int claimAreaId;

    @Column(name = "areatypeid")
    private int areaTypeId;

    @Column(name = "statusid")
    private int statusId;

}

