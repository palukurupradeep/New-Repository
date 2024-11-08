package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.DialCode;

import java.io.IOException;

public class DialCodeSerializer extends JsonSerializer<DialCode> {
    @Override
    public void serialize(DialCode dialCode, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("isoDialingCodeId");
        json.writeString(String.valueOf(dialCode.getIsoDialingCodeId()));
        json.writeFieldName("isoCountryDialingCode");
        json.writeString(dialCode.getIsoCountryDialingCode().toString());
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(dialCode.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(dialCode.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(dialCode.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(dialCode.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(dialCode.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
