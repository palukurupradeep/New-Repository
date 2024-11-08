package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
	private boolean fullService;
	@Column(name = "threepl")
	private boolean threePl;
	@Column(name = "geolocation", columnDefinition = "geography")
	private String geoLocation;
	@Column(name = "latitude")
	private double latitude;
	@Column(name = "longitude")
	private double longitude;
	@Column(name = "fulladdress")
	private String fullAddress;
	@Column(name = "statusid")
	private int statusId;
}
