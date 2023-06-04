package com.example.githubrepolistingapi.controller;

import com.example.githubrepolistingapi.model.GitHubRepository;
import com.example.githubrepolistingapi.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping(value = "/github/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GitHubRepository>> getUserRepositories(@PathVariable String username,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "30") int size
    ) {
        Page<GitHubRepository> repositoriesPage = gitHubService.getUserRepositoriesPage(username, page, size);

        return new ResponseEntity<>(repositoriesPage, HttpStatus.OK);
    }
}
