package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "resourcesmenu", schema = "mas")
public class ResourceMenu extends BaseEntity {

    @Id
    @Column(name = "resourcesmenuid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resourcesMenuId;

    @Column(name = "menuname")
    private String menuName;

    @Column(name = "menudescription")
    private String menuDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "parentmenuid")
    private ResourceMenu parentMenu;

    @OneToMany(mappedBy = "parentMenu", cascade = CascadeType.ALL)
    private List<ResourceMenu> subMenu = new ArrayList<>();

    @Column(name = "linkurl")
    private String linkUrl;

    @Column(name = "statusid")
    private Integer statusId;

}
