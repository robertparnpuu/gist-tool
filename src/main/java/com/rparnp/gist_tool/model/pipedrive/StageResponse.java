package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StageResponse {

    @JsonProperty
    private boolean success;
    @JsonProperty("data")
    private Data data;

    @lombok.Data
    public static class Data {

        @JsonProperty
        private Integer id;
        @JsonProperty("order_nr")
        private Integer orderNr;
        @JsonProperty("name")
        private String name;
        @JsonProperty("is_deleted")
        private boolean isDeleted;
        @JsonProperty("deal_probability")
        private Integer dealProbability;
        @JsonProperty("pipeline_id")
        private Integer pipelineId;
        @JsonProperty("is_deal_rot_enabled")
        private boolean isDealRotEnabled;
        @JsonProperty("days_to_rotten")
        private Integer daysToRotten;
        @JsonProperty("add_time")
        private String addTime;
        @JsonProperty("update_time")
        private String updateTime;
    }
}
