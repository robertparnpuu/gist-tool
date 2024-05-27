package com.rparnp.gist_tool.controller;

import com.rparnp.gist_tool.model.Gist;
import com.rparnp.gist_tool.service.GistService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/gist")
public class GistController {

    @Resource
    private GistService gistService;

    @GetMapping("/{username}")
    public ResponseEntity<List<Gist>> getGist(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(gistService.getGists(username));
    }
}
