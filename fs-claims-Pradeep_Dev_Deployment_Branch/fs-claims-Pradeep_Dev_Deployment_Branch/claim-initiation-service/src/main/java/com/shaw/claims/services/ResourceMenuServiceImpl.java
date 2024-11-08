package com.shaw.claims.services;

import java.time.LocalDateTime;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.shaw.claims.constant.CommonConstant;
import com.shaw.claims.dto.ResourceMenuDTO;
import com.shaw.claims.enums.StatusTypes;
import com.shaw.claims.exception.CommonException;
import com.shaw.claims.model.ResourceMenu;
import com.shaw.claims.repo.ResourceMenuRepository;
import com.shaw.claims.util.ObjectMapperUtil;

@Service
public class ResourceMenuServiceImpl implements ResourceMenuService {
	
	Logger log = LogManager.getLogger(ResourceMenuServiceImpl.class);
	
    @Autowired
    ResourceMenuRepository resourceMenuRepository;
    @Override
    public List<ResourceMenuDTO> fetchResourceMenus(String formName) {
        /* List<ResourceMenuDTO> resourceMenuDTOListList = menuDTO(formName);
        List<ResourceMenuDTO> tempDTOList = new ArrayList<>(resourceMenuDTOListList);
        List<ResourceMenuDTO> finalDTOList = new ArrayList<>(resourceMenuDTOListList);
        menuDtoList(resourceMenuDTOListList, tempDTOList,finalDTOList);*/
        return menuDTO(formName);
    }
    private void menuDtoList(List<ResourceMenuDTO> masterList,  List<ResourceMenuDTO> list, List<ResourceMenuDTO> finalList){
        if(!list.isEmpty()) {
            for(ResourceMenuDTO rm :masterList) {
                List<ResourceMenuDTO>  subMenuList = list.stream().filter(d-> Objects.equals(d.getParentMenuId(), rm.getResourcesMenuId())).toList();
                if(!subMenuList.isEmpty()) {
                    rm.setSubMenu(subMenuList);
                    if(!finalList.contains(rm))
                        finalList.add(rm);
                }
                menuDtoList(subMenuList, masterList, finalList);
            }
        }
    }
    private List<ResourceMenuDTO> menuDTO(String formName) {
		List<Integer> statusIds = formName != null && formName.equalsIgnoreCase("resourceMenu") ? List.of(1) : List.of(1,2,3);
        List<ResourceMenu> rm = resourceMenuRepository.fetchAllActiveResourceMenus(statusIds);
        return rm.stream().filter(m->m.getParentMenu().getResourcesMenuId() == -1)
                .map(this::createResourceMenuDTO)
                .toList();
    }
    private ResourceMenuDTO createResourceMenuDTO(ResourceMenu rm) {
    	return new ResourceMenuDTO(rm.getResourcesMenuId(), rm.getMenuName(), rm.getLinkUrl(),rm.getParentMenu().getResourcesMenuId(), createSubResMenuListDTO(rm.getSubMenu()), rm.getMenuDescription(),rm.getStatusId());
    }
	private List<ResourceMenuDTO> createSubResMenuListDTO(List<ResourceMenu> rmList){
		return rmList.stream().map(this::createResourceMenuDTO).toList();
	}
	@Override
	public ResourceMenuDTO addResourceMenu(ResourceMenuDTO resourceMenuDTO) {
		return updateResourceMenu(resourceMenuDTO);
	}
    @Override
    public ResourceMenuDTO updateResourceMenu(ResourceMenuDTO resourceMenuDTO) {
		ResourceMenu responseMenu = null;
		List<ResourceMenu> resourceMenuCheck=resourceMenuRepository.findByMenuName(resourceMenuDTO.getMenuName());
		if(resourceMenuDTO.getResourcesMenuId() == null && !resourceMenuCheck.isEmpty())
		{
			throw new CommonException(String.valueOf(HttpStatus.BAD_REQUEST),
					"Menu Name Already existed with:: "+resourceMenuDTO.getMenuName());
		}
		Optional<ResourceMenu> resourceMenu = resourceMenuRepository
				.findResourcesMenu(resourceMenuDTO.getResourcesMenuId(), resourceMenuDTO.getMenuName());
		if(resourceMenu.isPresent()){
			responseMenu = resourceMenu.get();
			BeanUtils.copyProperties(resourceMenuDTO, responseMenu, ObjectMapperUtil.getNullPropertyNames(resourceMenuDTO));
			if(resourceMenuDTO.getParentMenuId()!=null && !resourceMenuDTO.getParentMenuId().equals(responseMenu.getParentMenu().getResourcesMenuId()))
				responseMenu.setParentMenu(resourceMenuRepository.findByResourcesMenuId(resourceMenuDTO.getParentMenuId()).get());
			responseMenu.setModifiedByUserId(resourceMenuDTO.getModifiedByUserId());
		}else{
			responseMenu = new ResourceMenu();
			BeanUtils.copyProperties(resourceMenuDTO, responseMenu, ObjectMapperUtil.getNullPropertyNames(resourceMenuDTO));
			responseMenu.setCreatedByUserId(resourceMenuDTO.getCreatedByUserId());
			responseMenu.setParentMenu(resourceMenuRepository.findByResourcesMenuId(resourceMenuDTO.getParentMenuId()).get());
			responseMenu.setModifiedByUserId(resourceMenuDTO.getCreatedByUserId());
			responseMenu.setCreatedDateTime(LocalDateTime.now());
		}
		responseMenu.setStatusId(CommonConstant.ACTIVE);
		responseMenu.setModifiedDateTime(LocalDateTime.now());
		responseMenu = resourceMenuRepository.save(responseMenu);
		BeanUtils.copyProperties(responseMenu, resourceMenuDTO);
    	return resourceMenuDTO;
    }

    @Override
    public void deleteResourceMenu(Integer resourceMenuId) {
    	Optional<ResourceMenu> resourceMenu = resourceMenuRepository.findByResourcesMenuId(resourceMenuId);
    	if (resourceMenu.isPresent()) {
    		List<ResourceMenu> menuList = resourceMenuRepository.findByParentMenuId(resourceMenu.get().getResourcesMenuId(),StatusTypes.ACTIVE.getStatusId());
    		if (menuList != null && !menuList.isEmpty()) {
    			menuList.forEach(subMenu -> {
    				subMenu.setStatusId(CommonConstant.DELETED_STATUS_ID);
    				resourceMenuRepository.save(subMenu);
    			});
    		} else {
    			resourceMenu.get().setStatusId(CommonConstant.DELETED_STATUS_ID);
    			resourceMenuRepository.save(resourceMenu.get());
    		}
    	}
    }
	@Override
	public void enableordisableMenu(int resourcesMenuId, int statusId) {
		Optional<ResourceMenu> resourceMenu = resourceMenuRepository.findByResourcesMenuId(resourcesMenuId);
		int statusIdValue=StatusTypes.ACTIVE.getStatusId();
		if(StatusTypes.ACTIVE.getStatusId()==statusId)
		{
			statusIdValue=StatusTypes.INACTIVE.getStatusId();
		}
    	if (resourceMenu.isPresent()) {
    		List<ResourceMenu> menuList = resourceMenuRepository.findByParentMenuId(resourceMenu.get().getResourcesMenuId(),statusIdValue);
    		if (menuList != null && !menuList.isEmpty()) {
    			menuList.forEach(subMenu -> {
    				subMenu.setStatusId(statusId);
    				resourceMenuRepository.save(subMenu);
    			});
    		} else {
    			resourceMenu.get().setStatusId(statusId);
    			resourceMenuRepository.save(resourceMenu.get());
    		}
    	}
		
	}
    //will remove once we test end to end
    /*    @Override
    public List<ResourceMenu> fetchResourceMenus() {
        List<ResourceMenu> resourceMenuList = fetchAllActiveResourceMenus();
        List<ResourceMenu> tempList = new ArrayList<>(resourceMenuList);
        List<ResourceMenu> finalList = new ArrayList<>(resourceMenuList);
        menu(resourceMenuList, tempList,finalList);
        finalList.forEach(System.out::println);
        return null;
    }*/
/*    private void menu(List<ResourceMenu> masterList,  List<ResourceMenu> list, List<ResourceMenu> finalList){
       if(!list.isEmpty()) {
           for(ResourceMenu rm :masterList) {
               List<ResourceMenu>  subMenuList = list.stream().filter(d-> Objects.equals(d.getParentMenuId(), rm.getResourcesMenuId())).toList();
               //set the DTO list before recursion
               if(!subMenuList.isEmpty()) {
                   rm.setSubmenu(subMenuList);
                   if(!finalList.contains(rm))
                       finalList.add(rm);
               }
               menu(subMenuList, masterList, finalList);
           }
       }
    }*/
}
