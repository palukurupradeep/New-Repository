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
@Table(name = "workqueuefield", schema = "cnf")
public class WorkQueueField extends BaseEntity{

	@Id
    @Column(name = "workqueuefieldid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer workQueueFieldId;
	@Column(name = "workqueuefieldname")
    private String workQueueFieldName;
	@Column(name = "workqueuefielddescription")
    private String workQueueFieldDescription;
	@Column(name = "displaysequence")
    private Integer displaySequence;
	@Column(name = "displaybydefault")
	private boolean displayByDefault;
	@Column(name = "statusid")
    private Integer statusId;
}
