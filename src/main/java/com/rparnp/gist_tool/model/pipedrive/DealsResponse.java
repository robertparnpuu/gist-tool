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
    private List<Data> data;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {

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
}
