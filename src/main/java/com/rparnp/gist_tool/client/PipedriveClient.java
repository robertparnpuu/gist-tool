package com.rparnp.gist_tool.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rparnp.gist_tool.config.ToolConfig;
import com.rparnp.gist_tool.model.pipedrive.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class PipedriveClient {

    Logger logger = LoggerFactory.getLogger(PipedriveClient.class);

    private final String CONTENT_TYPE = "Content-type";
    private final String APPLICATION_JSON = "application/json";
    private final String APIKEY_PARAM = "api_token";
    private final String PIPELINE_ID_PARAM = "pipeline_id";
    private final String GET_STAGES_PATH = "/v2/stages";
    private final String CREATE_STAGE_PATH = "/v2/stages";
    private final String GET_DEALS_PATH = "/v1/deals";
    private final String CREATE_DEAL_PATH = "/v1/deals";

    @Autowired
    public ToolConfig toolConfig;

    public List<StagesResponse.Data> getAllStages() throws IOException, InterruptedException {
        logger.info("Getting all stages from pipeline");
        ObjectMapper objectMapper = new ObjectMapper();

        URI uri = UriComponentsBuilder.fromUriString(toolConfig.getPipedriveUri() + GET_STAGES_PATH)
                .queryParam(APIKEY_PARAM, toolConfig.getPipedriveToken())
                .queryParam(PIPELINE_ID_PARAM, toolConfig.getPipedrivePipelineId())
                .build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), StagesResponse.class).getData();
    }

    public List<DealsResponse.Data> getAllDeals() throws IOException, InterruptedException {
        logger.info("Getting all deals from pipeline");
        ObjectMapper objectMapper = new ObjectMapper();

        URI uri = UriComponentsBuilder.fromUriString(toolConfig.getPipedriveUri() + GET_DEALS_PATH)
                .queryParam(APIKEY_PARAM, toolConfig.getPipedriveToken()).build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), DealsResponse.class).getData();
    }

    public Integer createStage(String name) throws IOException, InterruptedException {
        logger.info("Creating stage: " + name);
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
        logger.info("Creating deal: " + deal.getTitle());
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
