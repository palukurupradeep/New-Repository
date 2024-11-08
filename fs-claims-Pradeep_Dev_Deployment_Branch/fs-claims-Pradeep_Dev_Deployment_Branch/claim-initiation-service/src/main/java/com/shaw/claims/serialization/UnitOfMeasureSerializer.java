package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.UnitOfMeasure;

import java.io.IOException;

public class UnitOfMeasureSerializer extends JsonSerializer<UnitOfMeasure> {
    @Override
    public void serialize(UnitOfMeasure unitOfMeasure, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("unitOfMeasureId");
        json.writeString(String.valueOf(unitOfMeasure.getUnitOfMeasureId()));
        json.writeFieldName("unitOfMeasureCode");
        json.writeString(unitOfMeasure.getUnitOfMeasureCode().toString());
        json.writeFieldName("unitOfMeasureDescription");
        json.writeString(unitOfMeasure.getUnitOfMeasureDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(unitOfMeasure.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(unitOfMeasure.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(unitOfMeasure.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(unitOfMeasure.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(unitOfMeasure.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(unitOfMeasure.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
