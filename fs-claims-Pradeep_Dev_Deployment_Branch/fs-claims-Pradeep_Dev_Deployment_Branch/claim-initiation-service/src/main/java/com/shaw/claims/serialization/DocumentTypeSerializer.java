package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.DocumentType;

import java.io.IOException;

public class DocumentTypeSerializer extends JsonSerializer<DocumentType> {
    @Override
    public void serialize(DocumentType documentType, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("documentTypeId");
        json.writeString(String.valueOf(documentType.getDocumentTypeId()));
        json.writeFieldName("documentTypeCode");
        json.writeString(documentType.getDocumentTypeCode().toString());
        json.writeFieldName("documentTypeDescription");
        json.writeString(documentType.getDocumentTypeDescription().toString());
        json.writeFieldName("displaySequence");
        json.writeString(String.valueOf(documentType.getDisplaySequence()));
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(documentType.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(documentType.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(documentType.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(documentType.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(documentType.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
