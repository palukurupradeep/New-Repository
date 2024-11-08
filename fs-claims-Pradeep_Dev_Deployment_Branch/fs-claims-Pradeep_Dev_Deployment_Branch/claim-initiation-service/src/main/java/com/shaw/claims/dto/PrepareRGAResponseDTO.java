package com.shaw.claims.dto;

import com.shaw.claims.model.Locations;
import lombok.Data;

import java.util.List;

@Data
public class PrepareRGAResponseDTO {
    private int rgaNumber;
    private Boolean issueRGA;
    private String codeRuleReturnType;
    private Locations RDCLocations;
    private String errorOccurred;
    private List<RGAResponseCodeAndErrorMsgDTO> codeAndErrorMsgDTOS;
}
