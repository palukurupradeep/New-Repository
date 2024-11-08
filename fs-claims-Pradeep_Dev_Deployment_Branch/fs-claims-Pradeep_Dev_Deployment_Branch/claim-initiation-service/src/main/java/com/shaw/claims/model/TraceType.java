package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tracetype", schema = "clm")
@Data
public class TraceType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracetypeid")
    private int traceTypeId;

    @Column(name = "tracetypecode")
    private String traceTypeCode;

    @Column(name = "tracetypedescription")
    private String traceTypeDescription;

    @Column(name = "tracetypedays")
    private int traceTypeDays;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "workstatusgroupid")
    private int workStatusGroupId;

    @Column(name = "statusid")
    private int statusId;

}

