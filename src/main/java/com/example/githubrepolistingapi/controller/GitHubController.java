package com.example.githubrepolistingapi.controller;

import com.example.githubrepolistingapi.model.GitHubRepository;
import com.example.githubrepolistingapi.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping(value = "/github/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GitHubRepository>> getUserRepositories(@PathVariable String username) {
        List<GitHubRepository> list = gitHubService.getUserRepositories(username);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
