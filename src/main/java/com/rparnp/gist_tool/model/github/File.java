package com.rparnp.gist_tool.model.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class File {
    @JsonProperty
    private String filename;
    @JsonProperty
    private String type;
    @JsonProperty
    private String language;
    @JsonProperty("raw_url")
    private String rawUrl;
    @JsonProperty
    private int size;
}
