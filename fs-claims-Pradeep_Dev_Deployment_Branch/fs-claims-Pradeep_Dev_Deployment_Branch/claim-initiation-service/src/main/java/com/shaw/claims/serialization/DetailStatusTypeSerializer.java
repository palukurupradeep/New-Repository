package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.DetailStatusType;
import com.shaw.claims.model.LineSource;

import java.io.IOException;

public class DetailStatusTypeSerializer extends JsonSerializer<DetailStatusType> {
    @Override
    public void serialize(DetailStatusType detailStatusType, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("detailStatusTypeId");
        json.writeString(String.valueOf(detailStatusType.getDetailStatusTypeId()));
        json.writeFieldName("detailStatusTypeCode");
        json.writeString(detailStatusType.getDetailStatusTypeCode().toString());
        json.writeFieldName("detailStatusTypeDescription");
        json.writeString(detailStatusType.getDetailStatusTypeDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(detailStatusType.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(detailStatusType.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(detailStatusType.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(detailStatusType.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(detailStatusType.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(detailStatusType.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
