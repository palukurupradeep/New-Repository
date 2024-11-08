package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.NoteTemplate;

import java.io.IOException;

public class NoteTemplateSerializer extends JsonSerializer<NoteTemplate> {
    @Override
    public void serialize(NoteTemplate noteTemplate, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("noteTemplateId");
        json.writeString(String.valueOf(noteTemplate.getNoteTemplateId()));
        json.writeFieldName("noteTemplateName");
        json.writeString(noteTemplate.getNoteTemplateName());
        json.writeFieldName("noteTemplateText");
        json.writeString(noteTemplate.getNoteTemplateText());


        json.writeFieldName("editable");
        json.writeBoolean(noteTemplate.isEditable());
        json.writeFieldName("isDefault");
        json.writeBoolean(noteTemplate.isDefault());
        json.writeFieldName("isManual");
        json.writeBoolean(noteTemplate.getIsManual());

        json.writeFieldName("auditHistoryTemplateText");
        json.writeString(noteTemplate.getAuditHistoryTemplateText());

        json.writeFieldName("statusId");
        json.writeString(String.valueOf(noteTemplate.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(noteTemplate.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(noteTemplate.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(noteTemplate.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(noteTemplate.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
