package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "claimbatchheader", schema = "CLM")
@Data
public class ClaimBatchHeader {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimbatchheaderid")
    private int claimBatchHeaderId;

    @Column(name = "usergroupid")
    private int userGroupId;

    @OneToOne
    @JoinColumn(name = "batchstatusid", referencedColumnName = "batchstatusid")
    private BatchStatus batchStatus;

    @Column(name = "batchdescription")
    private String batchDescription;

    @Column(name = "customerrollup")
    private boolean customerRollUp;

    @Column(name = "delivercreditmemo")
    private boolean deliverCreditMemo;

    @Column(name = "printbatch")
    private boolean printBatch;

    @Column(name = "createdbyuserid")
    private int createdByUserId;

    @Column(name = "processedbyuserid")
    private int processedByUserId;

    @Column(name = "createddatetime")
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @Column(name = "processeddatetime")
    private LocalDateTime processedDateTime = LocalDateTime.now();

    @OneToMany(mappedBy = "claimBatchHeader",cascade = CascadeType.ALL)
    private List<ClaimBatchDetail> claimBatchDetails;

}

