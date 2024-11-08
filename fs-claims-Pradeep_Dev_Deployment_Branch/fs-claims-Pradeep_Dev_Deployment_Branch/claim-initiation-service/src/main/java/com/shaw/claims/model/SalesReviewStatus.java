package com.shaw.claims.model;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "salesreviewstatus", schema = "clm")
@Data
public class SalesReviewStatus extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salesreviewstatusid")
    private int salesReviewStatusId;

    @Column(name = "salesreviewstatuscode")
    private String salesReviewStatusCode;

    @Column(name = "salesreviewstatusdescription")
    private String salesReviewStatusDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}

