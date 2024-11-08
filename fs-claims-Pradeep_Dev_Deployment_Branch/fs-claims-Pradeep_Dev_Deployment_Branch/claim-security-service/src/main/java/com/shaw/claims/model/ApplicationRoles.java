package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "applicationroles", schema = "mas")
public class ApplicationRoles extends BaseEntity {

	@Id
	@Column(name = "roleid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int roleId;
	@Column(name = "rolecode")
	private String roleCode;
	@Column(name = "roledescription")
	private String roleDescription;
	@Column(name = "statusid")
	private int statusId = 1;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "userrolemapping", schema = "mas", joinColumns = @JoinColumn(name = "roleid"),
			inverseJoinColumns = @JoinColumn(name = "userid"))
	private List<Users> users = new ArrayList<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "rolepermissionmapping", schema = "mas", joinColumns = @JoinColumn(name = "roleid"),
			inverseJoinColumns = @JoinColumn(name = "permissionid"))
	private List<ApplicationPermissions> permissions = new ArrayList<>();
}
