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
@Table(name = "userworkqueuefield", schema = "mas")
public class UserWorkQueueField extends BaseEntity {
	@Id
    @Column(name = "userworkqueuefieldid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userWorkQueueFieldId;
	@OneToOne(fetch  = FetchType.LAZY)
	@JoinColumn(name = "workqueuefieldid")
    private WorkQueueField workQueueField;
	@Column(name = "displaysequence")
    private Integer displaySequence;
	@Column(name = "statusid")
    private Integer statusId;
}
