package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "approvallimittype", schema = "clm")
public class ApprovalLimitType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approvallimittypeid")
    private int approvalLimitTypeId;

    @Column(name = "approvallimittypecode")
    private String approvalLimitTypeCode;

    @Column(name = "approvallimittypedescription")
    private String approvalLimitTypeDescription;

    @Column(name = "statusid")
    private int statusId;
}

