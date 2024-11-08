package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "workstatusgroup", schema = "clm")
public class WorkStatusGroup extends BaseEntity{
    @Id
    @Column(name = "workstatusgroupid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workStatusGroupId;
    @Column(name = "workstatusgroupcode")
    private String workStatusGroupCode;
    @Column(name = "workstatusgroupdescription")
    private String workStatusGroupDescription;
    @Column(name = "displaysequence")
    private int displaySequence;
    @Column(name = "claimstatusid")
    private int claimStatusId;
    @Column(name = "setclaimpriority")
    private int setClaimPriority;
    @Column(name = "statusid")
    private int statusId;
//    @OneToMany(mappedBy = "workStatusGroupDescription", cascade = CascadeType.ALL)
//    private WorkStatus workStatus;
}
