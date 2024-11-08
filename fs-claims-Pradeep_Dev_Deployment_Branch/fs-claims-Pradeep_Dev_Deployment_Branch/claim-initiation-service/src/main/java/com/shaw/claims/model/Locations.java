package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "locations", schema = "mas")
public class Locations extends BaseEntity {

    @Id
    @Column(name = "locationid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int locationId;
    @Column(name = "locationcode")
    private String locationCode;
    @Column(name = "locationname")
    private String locationName;
    @Column(name = "locationtype")
    private String locationType;
    @Column(name = "fullservice")
    private Boolean fullService;
    @Column(name = "threepl")
    private Boolean threePl;
    @Column(name = "geolocation", columnDefinition = "geography")
    private String geoLocation;
    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;
    @Column(name = "fulladdress")
    private String fullAddress;
    @Column(name = "postalcode")
    private String postalCode;
    @Column(name = "city")
    private String city;
    @Column(name = "statecode")
    private String stateCode;
    @Column(name = "statusid")
    private int statusId;
}
