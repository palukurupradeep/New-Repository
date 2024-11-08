package com.shaw.claims.dto;

import java.util.List;

import com.shaw.claims.model.BaseEntity;
import com.shaw.claims.model.WorkQueueField;

import lombok.Data;

@Data
public class UserWorkQueueFieldDTO extends BaseEntityDTO{

	private List<WorkQueueField> workQueueField;
	
}
