package com.rparnp.gist_tool.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Gist {

    @JsonProperty
    private String url;
    @JsonProperty("forks_url")
    private String forksUrl;
    @JsonProperty("commits_url")
    private String commitsUrl;
    @JsonProperty
    private String id;
    @JsonProperty("node_id")
    private String nodeId;
    @JsonProperty("git_pull_url")
    private String gitPullUrl;
    @JsonProperty("git_push_url")
    private String gitPushUrl;
    @JsonProperty("html_url")
    private String htmlUrl;
    @JsonProperty("public")
    private boolean isPublic;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty
    private String description;
    @JsonProperty
    private int comments;
    @JsonProperty
    private User user;
    @JsonProperty("comments_url")
    private String commentsUrl;
    @JsonProperty
    private User owner;
    @JsonProperty
    private boolean truncated;
}
