package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.ClaimCategorySerializer;
import com.shaw.claims.serialization.StatusSerializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "claimreasondefinition", schema = "clm")
public class ClaimReasonDefinition extends BaseEntity {

    @Id
    @Column(name = "claimreasonid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer claimReasonId;

    @Column(name = "claimreasoncode")
    private String claimReasonCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ClaimCategorySerializer.class)
    @JoinColumn(name = "claimcategoryid", referencedColumnName = "claimcategoryid")
    private ClaimCategory claimCategory;

    @Column(name = "visibilityscope")
    private Integer visibilityScope;

    @Column(name = "reasontypegroup")
    private Integer reasonTypeGroup;

    @Column(name = "isdistributioncompliance")
    private Boolean isDistributionCompliance;

    @Column(name = "samplesizerequirement")
    private String sampleSizeRequirement = "NOT DEFINED";

    @Column(name = "definition")
    private String definition;

    @Column(name = "cause")
    private String cause="NOT DEFINED";

    @Column(name = "testing")
    private String testing="NOT DEFINED";

    @JsonIgnore
    @JsonSerialize(using = StatusSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusid", referencedColumnName = "statusid")
    private Status status;
    
    @Column(name = "claimreasondescription")
    private String claimReasonDescription;

    @Column(name = "allowancetokeep")
    private boolean allowanceToKeep;

    @Column(name = "servicetokeep")
    private boolean serviceToKeep;

    @Column(name = "returneligible")
    private boolean returnEligible;

    @OneToOne
    @JoinColumn(name = "coderulereturntypeid", referencedColumnName = "coderulereturntypeid")
    private CodeRuleReturnType codeRuleReturnType;

}
