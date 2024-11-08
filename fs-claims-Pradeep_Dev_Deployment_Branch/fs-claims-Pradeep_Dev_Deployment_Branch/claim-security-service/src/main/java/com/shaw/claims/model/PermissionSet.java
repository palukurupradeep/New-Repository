package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "permissionsets", schema = "mas")
public class PermissionSet extends BaseEntity{
    @Id
    @Column(name = "permissionsetid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int permissionSetId;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "permissionid")
    private ApplicationPermissions permissions;
    @OneToOne
    @JoinColumn(name = "objectid", referencedColumnName = "objectid")
    private ApplicationObjects object;
    @OneToOne
    @JoinColumn(name = "functionid", referencedColumnName = "functionid")
    private ApplicationFunctions functions;
    @OneToOne
    @JoinColumn(name = "actionid", referencedColumnName = "actionid")
    private ApplicationActions actions;
    @Column(name = "statusid")
    private int statusId=1;
}
