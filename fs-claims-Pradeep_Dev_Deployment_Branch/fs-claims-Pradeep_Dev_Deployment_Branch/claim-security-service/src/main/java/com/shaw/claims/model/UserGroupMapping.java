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
@Table(name = "usergroupmapping", schema = "mas")
public class UserGroupMapping extends BaseEntity {
	@Id
	@Column(name = "usergroupmappingid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userGroupMappingId;
	@Column(name = "userid")
	private int userId;
	@Column(name = "usergroupid")
	private int userGroupId;
	@Column(name = "managerid")
	private int managerId;
	@Column(name = "statusid")
	private int statusId =1;
}
