package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.NoteTemplateSerializer;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vendorservicetype", schema = "clm")
@Data
public class VendorServiceType extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendorservicetypeid")
    private int vendorServiceTypeId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimvendorid")
    private ClaimVendor claimVendor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspectionservicetypeid")
    private InspectionServiceType inspectionServiceType;

    @Column(name = "statusid")
    private int statusId;

}