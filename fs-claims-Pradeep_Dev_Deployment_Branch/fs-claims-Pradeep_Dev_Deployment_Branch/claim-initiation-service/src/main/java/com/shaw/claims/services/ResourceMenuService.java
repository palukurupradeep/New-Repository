package com.shaw.claims.services;

import com.shaw.claims.dto.ResourceMenuDTO;

import java.util.List;

public interface ResourceMenuService {
	public List<ResourceMenuDTO> fetchResourceMenus(String formName);

	public ResourceMenuDTO updateResourceMenu(ResourceMenuDTO resourceMenuDTO);

	public void deleteResourceMenu(Integer resourceMenuId);

	public void enableordisableMenu(int resourcesMenuId, int statusId);

	public ResourceMenuDTO addResourceMenu(ResourceMenuDTO resourceMenuDTO);
}
