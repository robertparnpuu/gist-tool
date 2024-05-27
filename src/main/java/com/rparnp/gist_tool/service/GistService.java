package com.rparnp.gist_tool.service;

import com.rparnp.gist_tool.client.GitHubClient;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class GistService {

    @Resource
    private GitHubClient gitHubClient;

    public String getGists(String username) {
        String response;
        try {
            response = gitHubClient.getGists(username);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }
}
