package com.shaw.claims.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InStoreClaimLineDetailsResponseDTO {
    public int lineNumber;
    public String styleNumber;
    public String colorNumber;
    public String grade;
    public BigDecimal quantity;
    public String uom;
    public BigDecimal unitPrice;
    public BigDecimal salesDiscount;
    public String status;
}
