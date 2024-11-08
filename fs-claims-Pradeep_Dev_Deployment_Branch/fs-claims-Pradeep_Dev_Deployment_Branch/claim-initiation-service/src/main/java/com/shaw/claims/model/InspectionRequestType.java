package com.shaw.claims.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inspectionrequesttype", schema = "clm")
@Data
public class InspectionRequestType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inspectionrequesttypeid")
    private int inspectionRequestTypeId;

    @Column(name = "inspectionrequesttypecode")
    private String inspectionRequestTypeCode;

    @Column(name = "inspectionrequesttypename")
    private String inspectionRequestTypeName;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}

