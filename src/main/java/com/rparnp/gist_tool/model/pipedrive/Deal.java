package com.rparnp.gist_tool.model.pipedrive;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rparnp.gist_tool.model.github.File;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

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
    @JsonIgnore
    private Map<String, File> files;
}
