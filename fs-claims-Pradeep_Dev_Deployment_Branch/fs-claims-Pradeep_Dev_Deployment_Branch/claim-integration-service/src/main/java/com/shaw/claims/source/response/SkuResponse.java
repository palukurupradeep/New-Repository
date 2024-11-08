package com.shaw.claims.source.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkuResponse {
    private String Scac;
    private String Origin;
    private String Destination;
    private String Weight;
    private String cost;
    private String Rate;
    private String Savings;

}
