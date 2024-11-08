package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "workstatus", schema = "clm")
public class WorkStatus extends BaseEntity{
    @Id
    @Column(name = "workstatusid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workStatusId;
    @Column(name = "workstatuscode")
    private String workStatusCode;
    @Column(name = "workstatusdescription")
    private String workStatusDescription;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workstatusgroupid")
    @JsonIgnore
    private WorkStatusGroup workStatusGroupDescription;
    @Column(name = "displaysequence")
    private int displaySequence;
    @Column(name = "statusid")
    private int statusId;
}
