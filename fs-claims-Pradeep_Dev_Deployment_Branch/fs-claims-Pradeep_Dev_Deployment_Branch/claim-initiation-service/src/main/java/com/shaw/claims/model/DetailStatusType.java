package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detailstatustype", schema = "clm")
@Data
public class DetailStatusType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailstatustypeid")
    private int detailStatusTypeId;

    @Column(name = "detailstatustypecode")
    private String detailStatusTypeCode;

    @Column(name = "detailstatustypedescription")
    private String detailStatusTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}
