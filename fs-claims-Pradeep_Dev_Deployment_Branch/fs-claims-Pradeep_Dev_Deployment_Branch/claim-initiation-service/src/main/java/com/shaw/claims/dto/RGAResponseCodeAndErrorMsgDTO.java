package com.shaw.claims.dto;

import lombok.Data;

@Data
public class RGAResponseCodeAndErrorMsgDTO {
    private String errorMessage;
    private String reasonCode;
}
