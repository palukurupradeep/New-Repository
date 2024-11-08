package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.DetailType;
import com.shaw.claims.model.LineSource;

import java.io.IOException;

public class LineSourceSerializer extends JsonSerializer<LineSource> {
    @Override
    public void serialize(LineSource lineSource, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("lineSourceId");
        json.writeString(String.valueOf(lineSource.getLineSourceId()));
        json.writeFieldName("lineSourceCode");
        json.writeString(lineSource.getLineSourceCode().toString());
        json.writeFieldName("lineSourceDescription");
        json.writeString(lineSource.getLineSourceDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(lineSource.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(lineSource.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(lineSource.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(lineSource.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(lineSource.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(lineSource.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
