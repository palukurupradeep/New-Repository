package com.shaw.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceMenuDTO extends BaseEntityDTO{
    private Integer resourcesMenuId;
    private String menuName;
    private String linkUrl;
    private Integer parentMenuId;
    private String menuDescription;
    private List<ResourceMenuDTO> subMenu;
    private Integer statusId;


    public ResourceMenuDTO(Integer resourcesMenuId, String menuName, String linkUrl, Integer parentMenuId, String menuDescription,Integer statusId) {
		this.resourcesMenuId = resourcesMenuId;
		this.menuName = menuName;
		this.linkUrl = linkUrl;
		this.parentMenuId = parentMenuId;
		this.menuDescription = menuDescription;
		this.statusId = statusId;

    }


    public ResourceMenuDTO(Integer resourcesMenuId, String menuName, String linkUrl,Integer parentMenuId, List<ResourceMenuDTO> subMenu, String menuDescription,Integer statusId) {
        this.resourcesMenuId = resourcesMenuId;
        this.menuName = menuName;
        this.linkUrl = linkUrl;
        this.parentMenuId = parentMenuId;
        this.subMenu = subMenu;
        this.menuDescription = menuDescription;
        this.statusId = statusId;

    }
}
