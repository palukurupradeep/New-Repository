package com.shaw.claims.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.DialCodeSerializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "isocountries", schema = "cnf")
public class Country extends BaseEntity {
    @Id
    @Column(name = "isocountryid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer isoCountryId;
    @Column(name = "isocountrycode")
    private String isoCountryCode;
    @Column(name = "isocountrycode3")
    private String isoCountryCode3;
    @Column(name = "isocountryname")
    private String isoCountryName;
    @Column(name = "statusid")
    private Integer statusId;
    @JsonSerialize(using = DialCodeSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isodialingcodeid", referencedColumnName = "isodialingcodeid")
    private DialCode dialCode;
}
