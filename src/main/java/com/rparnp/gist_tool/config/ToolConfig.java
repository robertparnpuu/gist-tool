package com.rparnp.gist_tool.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Value("${github.uri}")
    public static String GITHUB_URI;

    @Value("${github.token}")
    public static String GITHUB_TOKEN;

}
