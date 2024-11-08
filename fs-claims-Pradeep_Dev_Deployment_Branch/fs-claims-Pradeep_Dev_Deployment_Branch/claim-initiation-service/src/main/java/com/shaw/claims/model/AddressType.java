package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresstype", schema = "clm")
@Data
public class AddressType extends  BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addresstypeid")
    private int addressTypeId;

    @Column(name = "addresstypecode")
    private String addressTypeCode;

    @Column(name = "addresstypedescription")
    private String addressTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}

