package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name = "rcscodes",schema = "clm")
@Data
public class RcsCodes extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rcscodeid")
    private int rcsCodeId;

    @Column(name = "rcscode")
    private String rcsCode;

    @Column(name = "rcscodedescription")
    private String rcsCodeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}
