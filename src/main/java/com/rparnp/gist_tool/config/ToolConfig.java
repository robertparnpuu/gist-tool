package com.rparnp.gist_tool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Value("${github.uri}")
    private String GITHUB_URI;
    @Value("${github.token}")
    private String GITHUB_TOKEN;
    @Value("${pipedrive.uri}")
    private String PIPEDRIVE_URI;
    @Value("${pipedrive.token}")
    private String PIPEDRIVE_TOKEN;
    @Value("${pipedrive.pipeline.id}")
    private Integer PIPEDRIVE_PIPELINE_ID;

    public String getGitHubUri() {
        return GITHUB_URI;
    }

    public String getGitHubToken() {
        return GITHUB_TOKEN;
    }

    public String getPipedriveUri() {
        return PIPEDRIVE_URI;
    }

    public String getPipedriveToken() {
        return PIPEDRIVE_TOKEN;
    }

    public Integer getPipedrivePipelineId() {
        return PIPEDRIVE_PIPELINE_ID;
    }
}
