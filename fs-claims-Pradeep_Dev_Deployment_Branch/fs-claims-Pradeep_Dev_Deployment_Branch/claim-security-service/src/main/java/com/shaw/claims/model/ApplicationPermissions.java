package com.shaw.claims.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "applicationpermissions", schema = "mas")
public class ApplicationPermissions extends BaseEntity {
	
	@Id
	@Column(name = "permissionid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int permissionId;
	@Column(name = "permissioncode")
	private String permissionCode;
	@Column(name = "permissiondescription")
	private String permissionDescription;
	@Column(name = "statusid")
	private int statusId=1;

/*	@JsonIgnore
	@OneToMany(mappedBy = "permissions")
	private List<PermissionSet> permissionSets = new ArrayList<>();*/

	@JsonIgnore
	@ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
	private List<ApplicationRoles> roles = new ArrayList<>();

	@OneToMany
	@JoinTable(name = "permissionsets", schema = "mas",
			joinColumns = @JoinColumn(name = "permissionid"),
			inverseJoinColumns = @JoinColumn(name = "objectid"))
	private Set<ApplicationObjects> applicationObjects = new HashSet<>();
	
}
