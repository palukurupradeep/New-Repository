package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.shaw.claims.model.UserDashboardWidget;

@Repository
public interface UserDashboardWidgetIdRepository extends JpaRepository<UserDashboardWidget, Integer>{
	@Modifying
	@Transactional
	@Query(value = "delete from UserDashboardWidget u WHERE u.createdByUserId=:createdByUserId")
	void deleteByUserDashboardWidgets(Integer createdByUserId);
	@Query("select u from UserDashboardWidget u where u.createdByUserId=:createdByUserId and u.statusId=1")
	List<UserDashboardWidget> findUserDashboardWidgetByUserId(Integer createdByUserId);

}
