package com.shaw.claims.dto;

import lombok.Data;

@Data
public class ResponseDTO {

    private Boolean isSuccess;
    private String message;
    private String errorCode;
    private Object data;
}
