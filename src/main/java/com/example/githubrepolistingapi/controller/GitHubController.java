package com.example.githubrepolistingapi.controller;

import com.example.githubrepolistingapi.model.GitHubRepository;
import com.example.githubrepolistingapi.service.GitHubService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;


    @Operation(summary = "Get user repositories", parameters = {
            @Parameter(name = "username", in = ParameterIn.PATH, required = true, description = "GitHub username"),
    })
    @GetMapping(value = "/github/user/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<GitHubRepository>> getUserRepositories(@PathVariable() String username,
                                                                      @RequestParam(defaultValue = "1") int page,
                                                                      @RequestParam(defaultValue = "30") int size
    ) {
        Page<GitHubRepository> repositoriesPage = gitHubService.getUserRepositoriesPage(username, page, size);

        return new ResponseEntity<>(repositoriesPage, HttpStatus.OK);
    }

}
