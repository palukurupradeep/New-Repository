package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "users", schema = "mas")
public class Users extends BaseEntity {
    @Id
    @Column(name = "userid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;
    @Column(name = "userkey")
    private int userKey;
    @Column(name = "username")
    private String userName;
    @Column(name = "activedirectoryid")
    private String activeDirectoryId;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "middleinitial")
    private Character middleInitial;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "racfid")
    private String racfId = "";
    @Column(name = "racfid2")
    private String racfId2 = "";
    @Column(name = "lid")
    private String lId = "";
    @Column(name = "lid2")
    private String lId2 = "";
    @Column(name = "statusid")
    private int statusId = 1;
    @Column(name = "emailaddress")
    private String emailAddress = "";
    @Column(name = "departmentname")
    private String departmentName;
    @Column(name = "title")
    private String title;
    @Column(name = "phonenumber")
    private String phoneNumber = "";
    @Column(name = "faxnumber")
    private String faxNumber = "";
    @Column(name = "objectid")
    private String objectId;
    @JsonIgnore
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<ApplicationRoles> roles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "usergroupmapping", schema = "mas", joinColumns = @JoinColumn(name = "userid"),
			inverseJoinColumns = @JoinColumn(name = "usergroupid"))
	private List<UserGroups> userGroups = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "userlocationmapping", schema = "mas", joinColumns = @JoinColumn(name = "userid"),
			inverseJoinColumns = @JoinColumn(name = "locationid"))
	private List<Locations> locations = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = false, fetch = FetchType.LAZY)
    List <UserApprovalLimit> userApprovalLimit = new ArrayList <> ();
}
