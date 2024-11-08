package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.LookupTypeSerializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "lookups", schema = "mas")
public class Lookup extends BaseEntity {

    @Id
    @Column(name = "lookupid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lookupId;

    @JsonIgnore
    @JsonSerialize(using = LookupTypeSerializer.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookuptypeid", referencedColumnName = "lookuptypeid")
    private LookupTypes lookupTypes;

    @Column(name = "lookupcode")
    private String lookupCode;

    @Column(name = "lookupdescription")
    private String lookupDescription;

    @Column(name = "displaysequence")
    private Integer displaySequence;

    @Column(name = "statusid")
    private Integer statusId;

}
