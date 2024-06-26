package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DealsResponse {

    @JsonProperty
    private boolean success;
    @JsonProperty
    private List<DealData> data;
}
