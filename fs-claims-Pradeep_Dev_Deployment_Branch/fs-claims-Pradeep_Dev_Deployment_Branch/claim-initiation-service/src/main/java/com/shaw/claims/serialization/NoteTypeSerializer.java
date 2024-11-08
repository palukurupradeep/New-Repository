package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.NoteType;

import java.io.IOException;

public class NoteTypeSerializer extends JsonSerializer<NoteType> {
    @Override
    public void serialize(NoteType noteType, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("noteTypeId");
        json.writeString(String.valueOf(noteType.getNoteTypeId()));
        json.writeFieldName("noteTypeCode");
        json.writeString(noteType.getNoteTypeCode());
        json.writeFieldName("noteTypeDescription");
        json.writeString(noteType.getNoteTypeDescription());

        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(noteType.getStatusId()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(noteType.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(noteType.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(noteType.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(noteType.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(noteType.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
