package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "claimrestockfee", schema = "clm")
public class ClaimRestockFee extends BaseEntity{

    @Id
    @Column(name = "claimrestockfeeid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer claimRestockFeeId;

    @Column(name = "claimrestockfeecode")
    private String claimRestockFeeCode;

    @Column(name = "claimrestockfeedescription")
    private String claimRestockFeeDescription;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}
