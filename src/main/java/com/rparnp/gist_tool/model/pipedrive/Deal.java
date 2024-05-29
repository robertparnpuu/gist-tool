package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Deal {

    @JsonProperty
    private String title;
    @JsonProperty("pipeline_id")
    private Integer pipelineId;
    @JsonProperty("stage_id")
    private Integer stageId;
    @JsonProperty("origin_id")
    private String originId;
}
