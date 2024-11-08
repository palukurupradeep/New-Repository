package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.DetailType;
import com.shaw.claims.model.RcsCodes;

import java.io.IOException;

public class DetailTypeSerializer extends JsonSerializer<DetailType> {
    @Override
    public void serialize(DetailType detailType, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("detailTypeId");
        json.writeString(String.valueOf(detailType.getDetailTypeId()));
        json.writeFieldName("detailTypeCode");
        json.writeString(detailType.getDetailTypeCode().toString());
        json.writeFieldName("detailTypeDescription");
        json.writeString(detailType.getDetailTypeDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(detailType.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(detailType.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(detailType.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(detailType.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(detailType.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(detailType.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
