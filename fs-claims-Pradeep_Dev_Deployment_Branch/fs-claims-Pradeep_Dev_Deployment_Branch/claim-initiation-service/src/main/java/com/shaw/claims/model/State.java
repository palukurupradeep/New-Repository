package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "isostates", schema = "cnf")
public class State extends BaseEntity {
    @Id
    @Column(name = "isostateid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer isoStateId;
    @Column(name = "isostatecode")
    private String isoStateCode;
    @Column(name = "isostatename")
    private String isoStateName;
    @Column(name = "isocountryid")
    private Integer isoCountryId;
    @Column(name = "statusid")
    private Integer statusId;
}
