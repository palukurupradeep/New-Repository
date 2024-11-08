package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "batchstatus", schema = "clm")
@Data
public class BatchStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "batchstatusid")
    private int batchStatusId;

    @Column(name = "batchstatuscode")
    private String batchStatusCode;

    @Column(name = "batchstatusdescription")
    private String batchStatusDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}

