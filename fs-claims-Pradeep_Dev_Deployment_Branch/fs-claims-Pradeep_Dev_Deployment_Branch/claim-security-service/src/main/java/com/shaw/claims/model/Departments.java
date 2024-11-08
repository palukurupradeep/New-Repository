package com.shaw.claims.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "departments", schema = "mas")
public class Departments extends BaseEntity {
    @Id
    @Column(name = "departmentid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int departmentId;
    @Column(name = "departmentcode")
    private String departmentCode;
    @Column(name = "departmentdescription")
    private String departmentDescription;
    @Column(name = "statusid")
    private int statusId;
}
