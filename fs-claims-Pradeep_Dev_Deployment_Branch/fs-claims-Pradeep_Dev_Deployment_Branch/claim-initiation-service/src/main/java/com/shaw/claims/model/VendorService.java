package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.NoteTemplateSerializer;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "vendorservice", schema = "clm")
@Data
public class VendorService extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vendorserviceid")
    private int vendorServiceId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "claimvendorid")
    private ClaimVendor claimVendor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspectionserviceid")
    private InspectionService inspectionService;

    @Column(name = "statusid")
    private int statusId;

}