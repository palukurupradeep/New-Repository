package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.SalesReviewStatus;

import java.io.IOException;

public class SalesReviewStatusSerializer extends JsonSerializer<SalesReviewStatus> {
    @Override
    public void serialize(SalesReviewStatus salesReviewStatus, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("salesReviewStatusId");
        json.writeString(String.valueOf(salesReviewStatus.getSalesReviewStatusId()));
        json.writeFieldName("salesReviewStatusCode");
        json.writeString(salesReviewStatus.getSalesReviewStatusCode().toString());
        json.writeFieldName("salesReviewStatusDescription");
        json.writeString(salesReviewStatus.getSalesReviewStatusDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(salesReviewStatus.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(salesReviewStatus.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(salesReviewStatus.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(salesReviewStatus.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(salesReviewStatus.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(salesReviewStatus.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
