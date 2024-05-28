package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Deal {

    @JsonProperty
    private String title;
    @JsonProperty
    private String value;
    @JsonProperty("pipeline_id")
    private Integer pipelineId;
    @JsonProperty("stage_id")
    private Integer stageId;
}
