package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.NoteGroup;

import java.io.IOException;

public class NoteGroupSerializer extends JsonSerializer<NoteGroup> {
    @Override
    public void serialize(NoteGroup noteGroup, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("noteGroupId");
        json.writeString(String.valueOf(noteGroup.getNoteGroupId()));
        json.writeFieldName("noteGroupCode");
        json.writeString(noteGroup.getNoteGroupCode());
        json.writeFieldName("noteGroupDescription");
        json.writeString(noteGroup.getNoteGroupDescription());
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(noteGroup.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(noteGroup.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(noteGroup.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(noteGroup.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(noteGroup.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
