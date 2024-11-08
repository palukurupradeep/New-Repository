package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detailtype", schema = "clm")
@Data
public class DetailType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailtypeid")
    private int detailTypeId;

    @Column(name = "detailtypecode")
    private String detailTypeCode;

    @Column(name = "detailtypedescription")
    private String detailTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}
