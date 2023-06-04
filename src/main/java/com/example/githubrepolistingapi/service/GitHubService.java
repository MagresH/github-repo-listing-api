package com.example.githubrepolistingapi.service;

import com.example.githubrepolistingapi.model.GitHubBranch;
import com.example.githubrepolistingapi.model.GitHubRepository;

import java.util.List;

public interface GitHubService {
    List<GitHubRepository> getUserRepositories(String username);
    List<GitHubBranch> getRepositoryBranches(String username, String repoName);
}
