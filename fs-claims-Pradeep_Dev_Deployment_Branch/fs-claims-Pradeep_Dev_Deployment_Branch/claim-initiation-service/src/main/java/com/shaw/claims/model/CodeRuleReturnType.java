package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "coderulereturntype", schema = "clm")
public class CodeRuleReturnType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coderulereturntypeid")
    private Integer codeRuleReturnTypeId;

    @Column(name = "coderulereturntypecode")
    private String codeRuleReturnTypeCode;

    @Column(name = "coderulereturntypedescription")
    private String codeRuleReturnTypeDescription;

    @Column(name = "displaysequence")
    private int displaySequence;

    @Column(name = "statusid")
    private int statusId;
}
