package com.shaw.claims.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonException extends RuntimeException {

	private String errorMessage;

	private String errorCode;

	private static final long serialVersionUID = 1L;

	public CommonException(String message) {
		super(message);
	}

	public CommonException(String errorCode, String errorMessage) {
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
}
