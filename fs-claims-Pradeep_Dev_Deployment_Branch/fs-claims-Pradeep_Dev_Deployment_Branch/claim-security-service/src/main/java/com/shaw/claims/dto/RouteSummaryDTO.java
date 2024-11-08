package com.shaw.claims.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteSummaryDTO {
    private int lengthInMeters;
    private int index;
}
