package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "declinereason", schema = "clm")
@Data
public class DeclineReason extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "declinereasonid")
    private int declineReasonId;

    @Column(name = "declinereasoncode")
    private String declineReasonCode;

    @Column(name = "declinereasondescription")
    private String declineReasonDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}
