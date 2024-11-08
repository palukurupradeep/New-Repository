package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.TraceType;

import java.io.IOException;

public class TraceTypeSerializer extends JsonSerializer<TraceType> {
    @Override
    public void serialize(TraceType traceType, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("traceTypeId");
        json.writeString(String.valueOf(traceType.getTraceTypeId()));
        json.writeFieldName("traceTypeCode");
        json.writeString(traceType.getTraceTypeCode());
        json.writeFieldName("traceTypeDescription");
        json.writeString(traceType.getTraceTypeDescription());
        json.writeFieldName("traceTypeDays");
        json.writeString(String.valueOf(traceType.getTraceTypeDays()));
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(traceType.getDisplaySequence()));
        json.writeFieldName("workStatusGroupId");
        json.writeString(String.valueOf(traceType.getWorkStatusGroupId()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(traceType.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(traceType.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(traceType.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(traceType.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(traceType.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
