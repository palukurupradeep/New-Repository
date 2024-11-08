package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detailrecordtype", schema = "clm")
@Data
public class DetailRecordType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailrecordtypeid")
    private int detailRecordTypeId;

    @Column(name = "detailrecordtypecode")
    private String detailRecordTypeCode;

    @Column(name = "detailrecordtypedescription")
    private String detailRecordTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}
