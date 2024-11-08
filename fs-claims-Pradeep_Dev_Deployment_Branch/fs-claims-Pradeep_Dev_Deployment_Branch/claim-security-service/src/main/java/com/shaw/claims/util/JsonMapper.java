package com.shaw.claims.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaw.claims.dto.RouteSummaryDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JsonMapper {

    public static List<RouteSummaryDTO> mapJsonToDTO(String jsonResponse) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode matrixNode = rootNode.path("matrix");

        List<RouteSummaryDTO> routeSummaries = new ArrayList<>();
        int index=0;
        for (JsonNode node : matrixNode) {
            for (JsonNode innerNode : node) {
                JsonNode routeSummaryNode = innerNode.path("response").path("routeSummary");
                int lengthInMeters = routeSummaryNode.path("lengthInMeters").asInt();
                RouteSummaryDTO dto = new RouteSummaryDTO(lengthInMeters, index);
                routeSummaries.add(dto);
                index++;
            }
        }
        index = 0;
        routeSummaries.sort(Comparator.comparingInt(RouteSummaryDTO::getLengthInMeters));
        return routeSummaries;
    }
}
