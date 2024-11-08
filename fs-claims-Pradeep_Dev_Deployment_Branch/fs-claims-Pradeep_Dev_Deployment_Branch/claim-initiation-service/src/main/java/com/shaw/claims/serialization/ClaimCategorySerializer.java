package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.ClaimCategory;

import java.io.IOException;

public class ClaimCategorySerializer extends JsonSerializer<ClaimCategory> {
    @Override
    public void serialize(ClaimCategory claimCategory, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("claimCategoryId");
        json.writeString(String.valueOf(claimCategory.getClaimCategoryId()));
        json.writeFieldName("claimCategoryCode");
        json.writeString(claimCategory.getClaimCategoryCode());
        json.writeFieldName("claimCategoryName");
        json.writeString(claimCategory.getClaimCategoryName());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(claimCategory.getDisplaySequence()));
        json.writeFieldName("status.statusId");
        json.writeString(String.valueOf(claimCategory.getStatus().getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(claimCategory.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(claimCategory.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(claimCategory.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(claimCategory.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
