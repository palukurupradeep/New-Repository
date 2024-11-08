package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "status", schema = "cnf")
public class Status extends BaseEntity {

    @Id
    @Column(name = "statusid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;

    @Column(name = "statuscode")
    private String statusCode;

    @Column(name = "statusdescription")
    private String statusDescription;

    @Column(name = "isactive")
    private Boolean isActive;
}
