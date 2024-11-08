package com.shaw.claims.enums;

import lombok.Getter;

@Getter
public enum Action {
	ASSIGN("assign"),
    ROUTE("route");
	private final String action;
	Action(String action) {
		this.action=action;
	}
}
