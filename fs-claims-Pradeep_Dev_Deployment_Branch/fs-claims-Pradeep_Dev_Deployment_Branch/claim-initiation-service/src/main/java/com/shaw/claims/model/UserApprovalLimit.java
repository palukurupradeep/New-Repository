package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "userapprovallimit", schema = "clm")
public class UserApprovalLimit extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userapprovallimitid")
    private int userApprovalLimitId;

    @Column(name = "userid")
    private int userId;

    @Column(name = "approvallimittypeid")
    private int approvalLimitTypeId;

    @Column(name = "approvallimit")
    private BigDecimal approvalLimit;

    @Column(name = "statusid")
    private int statusId;
}
