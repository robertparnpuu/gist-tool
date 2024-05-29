package com.rparnp.gist_tool.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rparnp.gist_tool.config.ToolConfig;
import com.rparnp.gist_tool.model.github.Gist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@Component
public class GitHubClient {

    Logger logger = LoggerFactory.getLogger(GitHubClient.class);

    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";
    private final String USER_GISTS_PATH = "users/{0}/gists";

    @Autowired
    public ToolConfig toolConfig;

    public List<Gist> getGists(String username) throws IOException, InterruptedException, URISyntaxException {
        logger.info("Getting gists for user: " + username);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(toolConfig.getGitHubUri() + MessageFormat.format(USER_GISTS_PATH, (Object[]) new String[]{username})))
                .header(AUTHORIZATION, BEARER + toolConfig.getGitHubToken())
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return Arrays.asList(objectMapper.readValue(response.body(), Gist[].class));
    }
}
