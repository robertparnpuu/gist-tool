package com.rparnp.gist_tool.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rparnp.gist_tool.config.ToolConfig;
import com.rparnp.gist_tool.model.github.File;
import com.rparnp.gist_tool.model.pipedrive.*;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

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
    private final String ADD_FILE = "/v1/files";

    @Autowired
    public ToolConfig toolConfig;
    @Resource
    public GitHubClient gitHubClient;

    ObjectMapper objectMapper = new ObjectMapper();

    public List<StagesResponse.Data> getAllStages() throws IOException, InterruptedException {
        logger.info("Getting all stages from pipeline");

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

    public List<DealData> getAllDeals() throws IOException, InterruptedException {
        logger.info("Getting all deals from pipeline");

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

    public DealResponse createDeal(Deal deal) throws IOException, InterruptedException {
        logger.info("Creating deal: " + deal.getTitle());

        URI uri = UriComponentsBuilder.fromUriString(toolConfig.getPipedriveUri() + CREATE_DEAL_PATH)
                .queryParam(APIKEY_PARAM, toolConfig.getPipedriveToken()).build().toUri();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(CONTENT_TYPE, APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(deal)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        return objectMapper.readValue(response.body(), DealResponse.class);
    }

    public void createFile(Integer dealId, File file) throws IOException, InterruptedException, URISyntaxException {
        logger.info("Uploading file: " + file.getFilename());
        byte[] fileContent = gitHubClient.getFile(file.getRawUrl());

        URI uri = UriComponentsBuilder.fromUriString(toolConfig.getPipedriveUri() + ADD_FILE)
                .queryParam(APIKEY_PARAM, toolConfig.getPipedriveToken()).build().toUri();

        String boundary = UUID.randomUUID().toString();
        byte[] formData = toFormData(fileContent, file.getFilename(), dealId, boundary);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(formData))
                .build();

        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private byte[] toFormData(byte[] fileContent, String fileName, Integer dealId, String boundary) throws IOException {
        String CRLF = "\r\n";

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        String boundaryLine = "--" + boundary + CRLF;

        stream.write(boundaryLine.getBytes(StandardCharsets.UTF_8));
        stream.write(("Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"" + CRLF)
                .getBytes(StandardCharsets.UTF_8));
        stream.write(("Content-Type: application/octet-stream" + CRLF + CRLF).getBytes(StandardCharsets.UTF_8));
        stream.write(fileContent);
        stream.write(CRLF.getBytes(StandardCharsets.UTF_8));

        stream.write(boundaryLine.getBytes(StandardCharsets.UTF_8));
        stream.write(("Content-Disposition: form-data; name=\"deal_id\"" + CRLF).getBytes(StandardCharsets.UTF_8));
        stream.write(("Content-Type: text/plain" + CRLF + CRLF).getBytes(StandardCharsets.UTF_8));
        stream.write(Integer.toString(dealId).getBytes(StandardCharsets.UTF_8));
        stream.write(CRLF.getBytes(StandardCharsets.UTF_8));

        stream.write(("--" + boundary + "--" + CRLF).getBytes(StandardCharsets.UTF_8));

        return stream.toByteArray();
    }
}
