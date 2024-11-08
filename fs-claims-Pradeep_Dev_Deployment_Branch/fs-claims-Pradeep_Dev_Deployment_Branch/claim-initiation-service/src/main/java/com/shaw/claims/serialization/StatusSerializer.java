package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.Status;

import java.io.IOException;

public class StatusSerializer extends JsonSerializer<Status> {
    @Override
    public void serialize(Status status, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("statusId");
        json.writeString(status.getStatusId().toString());
        json.writeFieldName("statusCode");
        json.writeString(status.getStatusCode().toString());
        json.writeFieldName("statusDescription");
        json.writeString(status.getStatusDescription().toString());
        json.writeFieldName("isActive");
        json.writeString(status.getIsActive().toString());
        json.writeFieldName("createdByUserId");
        json.writeString(status.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(status.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(status.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(status.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
