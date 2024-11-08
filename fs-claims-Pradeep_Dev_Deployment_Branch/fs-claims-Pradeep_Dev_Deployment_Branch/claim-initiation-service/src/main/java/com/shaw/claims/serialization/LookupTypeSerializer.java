package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.LookupTypes;

import java.io.IOException;

public class LookupTypeSerializer extends JsonSerializer<LookupTypes> {
    @Override
    public void serialize(LookupTypes lookupTypes, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("lookupTypeId");
        json.writeString(lookupTypes.getLookupTypeId().toString());
        json.writeFieldName("lookupTypeCode");
        json.writeString(lookupTypes.getLookupTypeCode().toString());
        json.writeFieldName("lookupTypeDescription");
        json.writeString(lookupTypes.getLookupTypeDescription().toString());
        json.writeFieldName("statusId");
        json.writeString(lookupTypes.getStatusId().toString());
        json.writeFieldName("createdByUserId");
        json.writeString(lookupTypes.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(lookupTypes.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(lookupTypes.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(lookupTypes.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
