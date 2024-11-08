package com.shaw.claims.dto;

import com.shaw.claims.model.Locations;
import lombok.Data;

import java.util.List;

@Data
public class RGAResponseDTO {
    private int rgaNumber;
    private String codeRuleReturnType;
    private Boolean issueRGA;
    private String sellingCompany;
    private String areaCode;
    private String region;
    private String division;
    private Locations RDCLocations;
    private List<RGAResponseCodeAndErrorMsgDTO> codeAndErrorMsgDTOS;
}
