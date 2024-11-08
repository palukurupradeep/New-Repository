package com.shaw.claims.dto;

import java.time.LocalDateTime;

import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntityDTO {


    private Integer createdByUserId;

    private Integer modifiedByUserId;
    
    private LocalDateTime createdDate;

}
