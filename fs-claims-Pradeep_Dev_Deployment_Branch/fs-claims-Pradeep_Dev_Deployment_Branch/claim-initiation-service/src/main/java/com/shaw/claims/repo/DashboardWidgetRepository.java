package com.shaw.claims.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shaw.claims.model.DashboardWidget;

@Repository
public interface DashboardWidgetRepository extends JpaRepository<DashboardWidget, Integer> {
@Query("select d from DashboardWidget d where d.statusId=1 and d.displaySequence>0")
 List<DashboardWidget> fetchDashboardWidgets();
}
