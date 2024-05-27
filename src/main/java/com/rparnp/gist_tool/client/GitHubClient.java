package com.rparnp.gist_tool.client;

import com.rparnp.gist_tool.config.ToolConfig;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;

@Component
public class GitHubClient {

    private final String AUTHORIZATION = "Authorization";
    private final String BEARER = "Bearer ";
    private final MessageFormat USER_GISTS_PATH = new MessageFormat("users/{0}/gists");

    public String getGists(String username) throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(ToolConfig.GITHUB_URI + USER_GISTS_PATH.format(username)))
                .header(AUTHORIZATION, BEARER + ToolConfig.GITHUB_TOKEN)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
