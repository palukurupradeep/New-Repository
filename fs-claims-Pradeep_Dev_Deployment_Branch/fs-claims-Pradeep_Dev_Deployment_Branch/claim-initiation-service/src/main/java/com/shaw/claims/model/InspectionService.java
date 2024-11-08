package com.shaw.claims.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inspectionservice", schema = "clm")
@Data
public class InspectionService extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspectionserviceid")
    private int inspectionServiceId;

    @Column(name = "inspectionservicecode")
    private String inspectionServiceCode;

    @Column(name = "inspectionservicename")
    private String inspectionServiceName;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}

