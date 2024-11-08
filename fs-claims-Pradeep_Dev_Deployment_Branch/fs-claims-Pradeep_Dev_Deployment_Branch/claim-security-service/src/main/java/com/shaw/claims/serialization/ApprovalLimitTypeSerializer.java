package com.shaw.claims.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.ApprovalLimitType;

import java.io.IOException;
public class ApprovalLimitTypeSerializer extends JsonSerializer<ApprovalLimitType> {

    @Override
    public void serialize(ApprovalLimitType approvalLimitType, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("approvallimittypeid");
        json.writeString(String.valueOf(approvalLimitType.getApprovalLimitTypeId()));
        json.writeFieldName("approvallimittypecode");
        json.writeString(approvalLimitType.getApprovalLimitTypeCode().toString());
        json.writeFieldName("approvallimittypedescription");
        json.writeString(approvalLimitType.getApprovalLimitTypeDescription().toString());
        json.writeFieldName("statusid");
        json.writeString(String.valueOf(approvalLimitType.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(approvalLimitType.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(approvalLimitType.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(approvalLimitType.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(approvalLimitType.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}
