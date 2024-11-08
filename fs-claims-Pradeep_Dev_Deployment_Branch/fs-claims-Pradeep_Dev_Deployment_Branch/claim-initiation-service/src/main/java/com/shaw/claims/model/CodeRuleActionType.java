package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coderuleactiontype", schema = "clm")
@Data
public class CodeRuleActionType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coderuleactiontypeid")
    private int codeRuleActionTypeid;

    @Column(name = "coderuleactiontypecode")
    private String codeRuleActionTypeCode;
    
    @Column(name = "coderuleactiontypedescription")
    private String codeRuleActionTypeDescription;
    
    @Column(name = "displaysequence")
    private int displaySequence;
    
    @Column(name = "statusid")
    private int statusId;

}
