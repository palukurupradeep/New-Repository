package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.DetailRecordType;

import java.io.IOException;

public class DetailRecordTypeSerializer extends JsonSerializer<DetailRecordType> {
    @Override
    public void serialize(DetailRecordType detailRecordType, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("detailRecordTypeId");
        json.writeString(String.valueOf(detailRecordType.getDetailRecordTypeId()));
        json.writeFieldName("detailRecordTypeCode");
        json.writeString(detailRecordType.getDetailRecordTypeCode().toString());
        json.writeFieldName("detailRecordTypeDescription");
        json.writeString(detailRecordType.getDetailRecordTypeDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(detailRecordType.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(detailRecordType.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(detailRecordType.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(detailRecordType.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(detailRecordType.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(detailRecordType.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
