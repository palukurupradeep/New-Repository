package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "userdashboardwidget", schema = "mas")
public class UserDashboardWidget extends BaseEntity{

	@Id
    @Column(name = "userdashboardwidgetid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userDashboardWidgetId;
	@OneToOne(fetch  = FetchType.LAZY)
	@JoinColumn(name = "dashboardwidgetid")
    private DashboardWidget dashboardWidget;
	@Column(name = "displaysequence")
	private Integer displaySequence;
	 @Column(name = "statusid")
	private Integer statusId;
}
