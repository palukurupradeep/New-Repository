package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "lineaddreason", schema = "clm")
@Data
public class LineAddReason extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lineaddreasonid")
    private int lineAddReasonId;

    @Column(name = "lineaddreasoncode")
    private String lineAddReasonCode;

    @Column(name = "lineaddreasondescription")
    private String lineAddReasonDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;

}
