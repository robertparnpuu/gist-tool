package com.rparnp.gist_tool.controller;

import com.rparnp.gist_tool.exceptions.NetworkException;
import com.rparnp.gist_tool.model.github.Gist;
import com.rparnp.gist_tool.service.GistService;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/upload/{username}")
    public ResponseEntity<String> uploadGists(@PathVariable String username) {
        gistService.uploadGists(username);
        return ResponseEntity.status(HttpStatus.OK).body("Success");
    }

    @ExceptionHandler({NetworkException.class})
    public ResponseEntity<String> handleNetworkException(RuntimeException ex) {
        System.out.println();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
