package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "areatype", schema = "clm")
@Data
public class AreaType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "areatypeid")
    private Integer areaTypeId;

    @Column(name = "areatypecode")
    private String areaTypeCode;

    @Column(name = "areatypedescription")
    private String areaTypeDescription;

    @Column(name = "priority")
    private int priority;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}
