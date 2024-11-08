package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RDCPlantIDResponse {
    private String rGAZipCode;
    private String Plant;
    private String AddressLine1;
    private String AddressLine2;
    private String City;
    private String StateCode;
    private String RDCZipCode;

}
