package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DealData {

    @JsonProperty
    private Integer id;
    @JsonProperty
    private String title;
    @JsonProperty("add_time")
    private String addTime;
    @JsonProperty("update_time")
    private String updateTime;
    @JsonProperty("origin_id")
    private String originId;
}
