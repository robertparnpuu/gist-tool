package com.rparnp.gist_tool.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rparnp.gist_tool.config.ToolConfig;
import com.rparnp.gist_tool.model.pipedrive.Deal;
import com.rparnp.gist_tool.model.pipedrive.Stage;
import com.rparnp.gist_tool.model.pipedrive.StageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class PipedriveClient {

    private final String CONTENT_TYPE = "Content-type";
    private final String APPLICATION_JSON = "application/json";
    private final String APIKEY_PARAM = "api_token";
    private final String CREATE_STAGE_PATH = "/v2/stages";
    private final String CREATE_DEAL_PATH = "/v1/deals";

    @Autowired
    public ToolConfig toolConfig;

    public Integer createStage(String name) throws IOException, InterruptedException {
        ObjectMapper objectMapper = new ObjectMapper();
        Stage stage = new Stage(name, toolConfig.getPipedrivePipelineId());
        URI uri = UriComponentsBuilder.fromUriString(toolConfig.getPipedriveUri() + CREATE_STAGE_PATH)
                .queryParam(APIKEY_PARAM, toolConfig.getPipedriveToken()).build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(stage)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        StageResponse responseBody = objectMapper.readValue(response.body(), StageResponse.class);
        return responseBody.getData().getId();
    }

    public void createDeal(Deal deal) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        URI uri = UriComponentsBuilder.fromUriString(toolConfig.getPipedriveUri() + CREATE_DEAL_PATH)
                .queryParam(APIKEY_PARAM, toolConfig.getPipedriveToken()).build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(deal)))
                .build();
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }
}
