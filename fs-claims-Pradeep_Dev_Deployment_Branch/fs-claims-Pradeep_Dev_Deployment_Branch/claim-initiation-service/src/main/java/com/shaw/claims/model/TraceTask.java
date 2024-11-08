package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.TraceTypeSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tracetask", schema = "clm")
@Data
public class TraceTask extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracetaskid")
    private int traceTaskId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    @JsonSerialize(using = TraceTypeSerializer.class)
    @JoinColumn(name = "tracetypeid")
    private TraceType traceType;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "claimnoteid")
    @JsonIgnore
    private ClaimNote claimNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimid")
    @JsonIgnore
    private Claim claim;

    @Column(name = "tracedate")
    private LocalDateTime traceDate;

    @Column(name = "assigneduserid")
    private int assignedUserId;

    @Column(name = "tracereference")
    private String traceReference;

    @Column(name = "traceactionlink")
    private String traceActionLink;

    @Column(name = "statusid")
    private int statusId;
}

