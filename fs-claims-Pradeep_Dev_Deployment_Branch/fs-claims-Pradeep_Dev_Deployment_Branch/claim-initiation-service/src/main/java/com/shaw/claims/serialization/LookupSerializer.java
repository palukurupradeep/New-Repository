package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.Lookup;

import java.io.IOException;

public class LookupSerializer extends JsonSerializer<Lookup> {
    @Override
    public void serialize(Lookup lookup, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("lookupId");
        json.writeString(String.valueOf(lookup.getLookupId()));

        json.writeFieldName("lookupTypes.lookupTypeId");
        json.writeString(String.valueOf(lookup.getLookupTypes().getLookupTypeId()));

        json.writeFieldName("lookupCode");
        json.writeString(lookup.getLookupCode());
        json.writeFieldName("lookupDescription");
        json.writeString(lookup.getLookupDescription());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(lookup.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(lookup.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(lookup.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(lookup.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(lookup.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(lookup.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
