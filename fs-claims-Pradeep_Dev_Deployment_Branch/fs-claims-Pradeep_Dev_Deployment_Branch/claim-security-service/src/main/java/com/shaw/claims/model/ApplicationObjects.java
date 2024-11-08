package com.shaw.claims.model;

import java.util.*;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "applicationobjects", schema = "cnf")
public class ApplicationObjects extends BaseEntity {

	@Id
	@Column(name = "objectid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int objectId;

	@Column(name = "objectcode")
	private String objectCode;

	@Column(name = "objectdescription")
	private String objectDescription;

	@Column(name = "statusid")
	private int statusId;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "objectfunctionmapping", schema = "cnf", joinColumns = @JoinColumn(name = "objectid"),
			inverseJoinColumns = @JoinColumn(name = "functionid"))
	private List<ApplicationFunctions> applicationFunctions = new ArrayList<>();
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "objectactionmapping", schema = "cnf", joinColumns = @JoinColumn(name = "objectid"),
			inverseJoinColumns = @JoinColumn(name = "actionid"))
	private List<ApplicationActions> applicationActions = new ArrayList<>();

	@OneToMany
	@JoinTable(name = "permissionsets", schema = "mas",
			joinColumns = @JoinColumn(name = "objectid"),
			inverseJoinColumns = @JoinColumn(name = "functionid"))
	private Set<ApplicationFunctions> viewFunctions = new HashSet<>();
	@OneToMany
	@JoinTable(name = "permissionsets", schema = "mas",
			joinColumns = @JoinColumn(name = "objectid"),
			inverseJoinColumns = @JoinColumn(name = "actionid"))
	private Set<ApplicationActions> viewActions = new HashSet<>();

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ApplicationObjects that)) return false;
		if (!super.equals(o)) return false;
		return objectId == that.objectId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), objectId);
	}
}
