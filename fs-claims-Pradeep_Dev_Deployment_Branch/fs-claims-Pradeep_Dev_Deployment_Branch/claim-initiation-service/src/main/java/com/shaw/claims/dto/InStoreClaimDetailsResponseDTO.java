package com.shaw.claims.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class InStoreClaimDetailsResponseDTO {
    public String invoiceNumber;
    public LocalDateTime invoiceDate;
    public String orderNumber;
    public LocalDateTime orderDate;
    public String poNumber;
    public String shippingPoint;
    public List<InStoreClaimLineDetailsResponseDTO> inStoreClaimLineDetailsResponseDTOS;
}
