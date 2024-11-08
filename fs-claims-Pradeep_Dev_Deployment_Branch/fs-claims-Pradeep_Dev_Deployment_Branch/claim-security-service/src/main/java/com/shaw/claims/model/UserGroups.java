package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "usergroups", schema = "mas")
public class UserGroups extends BaseEntity{
	@Id
	@Column(name = "usergroupid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userGroupId;
	@Column(name = "usergroupcode")
	private String userGroupCode;
	@Column(name = "usergroupdescription")
	private String userGroupDescription;
	@Column(name = "statusid")
	private int statusId = 1;
	@Column(name = "claimassignment")
	private int claimAssignment;
	@Column(name = "claimrouting")
	private int claimRouting;
	@Column(name = "genericassignment")
	private int genericAssignment;
	@JsonIgnore
	@ManyToMany(mappedBy = "userGroups", fetch = FetchType.LAZY)
	@OrderBy("firstName ASC")
	private List<Users> users = new ArrayList<>();
}
