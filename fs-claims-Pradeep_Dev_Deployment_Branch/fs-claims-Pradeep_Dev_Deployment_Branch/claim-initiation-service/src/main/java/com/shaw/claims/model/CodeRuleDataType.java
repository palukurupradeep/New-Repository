package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "coderuledatatype", schema = "clm")
@Data
public class CodeRuleDataType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coderuledatatypeid")
    private int codeRuleDataTypeid;

    @Column(name = "coderuledatatypecode")
    private String codeRuleDataTypeCode;
    
    @Column(name = "coderuledatatypedescription")
    private String codeRuleDataTypeDescription;
    
    @Column(name = "dbdatatype")
    private String dbDataType;
    
    @Column(name = "displaysequence")
    private int displaySequence;
    
    @Column(name = "statusid")
    private int statusId;

}
