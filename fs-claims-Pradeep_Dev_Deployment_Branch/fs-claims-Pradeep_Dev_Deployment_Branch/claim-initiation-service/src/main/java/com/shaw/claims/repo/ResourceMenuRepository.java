package com.shaw.claims.repo;

import com.shaw.claims.model.ResourceMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceMenuRepository extends JpaRepository<ResourceMenu, Integer> {
	@Query("SELECT rm FROM ResourceMenu rm where rm.parentMenu.resourcesMenuId =:menuId AND rm.resourcesMenuId > 0 AND rm.statusId = 1")
	List<ResourceMenu> fetchResourceMenusByMenuId(Integer menuId);
	@Query("SELECT rm FROM ResourceMenu rm where rm.statusId IN (:statusIds)")
	List<ResourceMenu> fetchAllActiveResourceMenus(List<Integer> statusIds);
	Optional<ResourceMenu> findByResourcesMenuId(int resourcesMenuId);
	@Query("SELECT rm FROM ResourceMenu rm where rm.resourcesMenuId =:resourcesMenuId AND rm.menuName =:menuName")
	Optional<ResourceMenu> findResourcesMenu(Integer resourcesMenuId, String menuName);
	@Query("SELECT rm FROM ResourceMenu rm where rm.parentMenu.resourcesMenuId=:resourceMenuId OR rm.resourcesMenuId=:resourceMenuId AND rm.statusId=:statusId")
	List<ResourceMenu> findByParentMenuId(Integer resourceMenuId,Integer statusId);
	List<ResourceMenu> findByMenuName(String menuName);
}
