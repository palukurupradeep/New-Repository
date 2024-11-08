package com.shaw.claims.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class NoteTemplateDTO extends BaseEntityDTO{

	private Integer noteTemplateId;
	private String noteTemplateName;
	private String noteTemplateText;
	private Integer noteGroupId;
	private String noteGroupName;
	private Integer noteTypeId;
	private String noteTypeName;
	private Boolean editable;
	private Boolean isDefault;
	private Boolean isManual;
	private String auditHistoryTemplateText;

}
