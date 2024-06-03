package com.rparnp.gist_tool.model.firestore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GistEntry {

    private String id;
    private String name;
    private String creator;
}
