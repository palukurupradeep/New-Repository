package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "isodialcodes", schema = "cnf")
public class DialCode extends BaseEntity{
    @Id
    @Column(name = "isodialingcodeid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer isoDialingCodeId;
    @Column(name = "isocountrydialingcode")
    private String isoCountryDialingCode;
    @Column(name = "statusid")
    private Integer statusId;
}
