package com.shaw.claims.serialization;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.shaw.claims.model.JobConfiguration;
import java.io.IOException;

public class JobConfigurationSerializer extends JsonSerializer<JobConfiguration> {

    @Override
    public void serialize(JobConfiguration jobConfiguration, JsonGenerator json, SerializerProvider serializers) throws IOException {
        json.writeStartObject();
        json.writeFieldName("configurationid");
        json.writeString(String.valueOf(jobConfiguration.getConfigurationId()));
        json.writeFieldName("configurationkey");
        json.writeString(jobConfiguration.getConfigurationKey());
        json.writeFieldName("configurationvalue");
        json.writeString(jobConfiguration.getConfigurationValue());
        json.writeFieldName("statusid");
        json.writeString(String.valueOf(jobConfiguration.getStatusId()));
        json.writeFieldName("createdByUserId");
        json.writeString(jobConfiguration.getCreatedByUserId().toString());
        json.writeFieldName("modifiedByUserId");
        json.writeString(jobConfiguration.getModifiedByUserId().toString());
        json.writeFieldName("createdDateTime");
        json.writeString(jobConfiguration.getCreatedDateTime().toString());
        json.writeFieldName("modifiedDateTime");
        json.writeString(jobConfiguration.getModifiedDateTime().toString());
        json.writeEndObject();
    }
}

