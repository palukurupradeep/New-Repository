package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "coderesolutions", schema = "clm")
public class ClaimCodeResolution extends BaseEntity{


    @Id
    @Column(name = "coderesolutionid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codeResolutionId;

    @OneToOne
    @JoinColumn(name = "claimreasonid", referencedColumnName = "claimreasonid")
    private ClaimReasonDefinition claimReasonDefinition;
    
    @OneToOne
    @JoinColumn(name = "resolutionid", referencedColumnName = "resolutionid")
    private ClaimResolution claimResolution;

    @Column(name = "statusid")
    private int status;

   
}
