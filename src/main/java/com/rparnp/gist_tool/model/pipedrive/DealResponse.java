package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DealResponse {

    @JsonProperty
    private boolean success;
    @JsonProperty
    private DealData data;
}
