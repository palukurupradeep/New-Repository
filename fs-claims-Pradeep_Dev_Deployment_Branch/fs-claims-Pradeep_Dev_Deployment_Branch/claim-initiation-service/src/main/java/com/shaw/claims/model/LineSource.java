package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "linesource",schema = "clm")
@Data
public class LineSource extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "linesourceid")
    private int lineSourceId;

    @Column(name = "linesourcecode")
    private String lineSourceCode;

    @Column(name = "linesourcedescription")
    private String lineSourceDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}
