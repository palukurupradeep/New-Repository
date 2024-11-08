package com.shaw.claims.enums;

public enum StatusTypes {
	ACTIVE(1),
    INACTIVE(2),
    DELETE(3),
	PENDING(4);

	 private Integer statusId;

	StatusTypes(int statusId) {
		this.statusId=statusId;
	}

	public Integer getStatusId() {
		return statusId;
	}
	
}
