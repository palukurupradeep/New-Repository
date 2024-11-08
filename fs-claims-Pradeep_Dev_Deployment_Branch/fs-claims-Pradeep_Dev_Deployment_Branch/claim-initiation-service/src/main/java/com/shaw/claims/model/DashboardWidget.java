package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "dashboardwidget", schema = "cnf")
public class DashboardWidget extends BaseEntity{

	 @Id
    @Column(name = "dashboardwidgetid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int dashboardWidgetId;
	@Column(name = "dashboardwidgetname")
	private String dashboardWidgetName;
	@Column(name = "dashboardwidgetdescription")
	private String dashboardWidgetDescription;
	@Column(name = "displaysequence")
    private Integer displaySequence;
	@Column(name = "displaybydefault")
	private boolean displayByDefault;
	@Column(name = "statusid")
    private Integer statusId;
}
