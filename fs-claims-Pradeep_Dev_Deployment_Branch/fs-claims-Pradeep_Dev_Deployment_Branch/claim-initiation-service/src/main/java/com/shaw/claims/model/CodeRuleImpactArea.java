package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coderuleimpactarea", schema = "clm")
@Data
public class CodeRuleImpactArea extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coderuleimpactareaid")
    private int codeRuleImpactAreaId;

    @Column(name = "coderuleimpactareacode")
    private String codeRuleImpactAreaCode;
    
    @Column(name = "coderuleimpactareadescription")
    private String codeRuleImpactAreaDescription;
    
    @Column(name = "displaysequence")
    private int displaySequence;
    
    @Column(name = "statusid")
    private int statusId;

}
