package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "lookuptypes", schema = "mas")
public class LookupTypes extends BaseEntity {

    @Id
    @Column(name = "lookuptypeid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lookupTypeId;

    @Column(name = "lookuptypecode")
    private String lookupTypeCode;

    @Column(name = "lookuptypedescription")
    private String lookupTypeDescription;

    @Column(name = "statusid")
    private Integer statusId;
}
