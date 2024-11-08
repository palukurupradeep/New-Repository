package com.shaw.claims.dto;

import lombok.Data;

@Data
public class SecondarySearchDTO {

	private int lookupId;
    private String lookupCode;
    private String lookupDescription;
    private int displaySequence;
    private int statusId;
    private int primarySearchId;
}
