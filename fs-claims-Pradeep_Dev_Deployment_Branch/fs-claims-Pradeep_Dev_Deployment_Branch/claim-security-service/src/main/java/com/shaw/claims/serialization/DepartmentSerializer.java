package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.Departments;

import java.io.IOException;
public class DepartmentSerializer extends JsonSerializer<Departments> {
    @Override
    public void serialize(Departments departments, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("departmentid");
        json.writeString(String.valueOf(departments.getDepartmentId()));
        json.writeFieldName("departmentcode");
        json.writeString(departments.getDepartmentCode().toString());
        json.writeFieldName("departmentdescription");
        json.writeString(departments.getDepartmentDescription().toString());
        json.writeFieldName("statusId");
        json.writeString(String.valueOf(departments.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(departments.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(departments.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(departments.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(departments.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
