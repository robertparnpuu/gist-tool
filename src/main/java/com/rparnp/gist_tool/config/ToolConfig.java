package com.rparnp.gist_tool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Value("${github.uri}")
    private String GITHUB_URI;

    @Value("${github.token}")
    private String GITHUB_TOKEN;

    public String getGitHubUri() {
        return GITHUB_URI;
    }

    public String getGitHubToken() {
        return GITHUB_TOKEN;
    }
}
