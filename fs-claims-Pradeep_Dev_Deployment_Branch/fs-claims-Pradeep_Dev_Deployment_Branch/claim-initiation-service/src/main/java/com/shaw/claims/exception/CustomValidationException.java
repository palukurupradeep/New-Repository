package com.shaw.claims.exception;

import com.shaw.claims.dto.ResponseDTO;

public class CustomValidationException extends RuntimeException {
    private String errorMessage;
    private String errorCode;

    public CustomValidationException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
