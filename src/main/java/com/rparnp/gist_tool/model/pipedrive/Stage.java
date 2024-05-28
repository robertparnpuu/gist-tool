package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Stage {

    @JsonProperty
    private String name;
    @JsonProperty("pipeline_id")
    private Integer pipelineId;
}
