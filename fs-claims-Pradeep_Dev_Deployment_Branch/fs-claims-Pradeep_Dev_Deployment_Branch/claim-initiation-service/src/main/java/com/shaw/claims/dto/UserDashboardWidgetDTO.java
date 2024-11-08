package com.shaw.claims.dto;

import java.util.List;

import com.shaw.claims.model.DashboardWidget;

import lombok.Data;
@Data
public class UserDashboardWidgetDTO  extends BaseEntityDTO{

	private List<DashboardWidget> dashboardWidget;
}
