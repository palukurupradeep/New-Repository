package com.shaw.claims.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class BaseEntity {

    @Column(name = "createdbyuserid")
    private Integer createdByUserId;

    @Column(name = "modifiedbyuserid")
    private Integer modifiedByUserId;

    @Column(name = "createddatetime", columnDefinition = "datetime")
    private LocalDateTime createdDateTime = LocalDateTime.now();

    @Column(name = "modifieddatetime", columnDefinition = "datetime")
    private LocalDateTime modifiedDateTime = LocalDateTime.now();

}
