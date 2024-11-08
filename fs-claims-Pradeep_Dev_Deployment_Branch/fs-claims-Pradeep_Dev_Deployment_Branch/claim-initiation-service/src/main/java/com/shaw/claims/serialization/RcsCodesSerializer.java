package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.RcsCodes;

import java.io.IOException;

public class RcsCodesSerializer extends JsonSerializer<RcsCodes> {
    @Override
    public void serialize(RcsCodes rcsCodes, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("rcsCodeId");
        json.writeString(String.valueOf(rcsCodes.getRcsCodeId()));
        json.writeFieldName("rcsCode");
        json.writeString(rcsCodes.getRcsCode().toString());
        json.writeFieldName("rcsCodeDescription");
        json.writeString(rcsCodes.getRcsCodeDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(rcsCodes.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(rcsCodes.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(rcsCodes.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(rcsCodes.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(rcsCodes.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(rcsCodes.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
