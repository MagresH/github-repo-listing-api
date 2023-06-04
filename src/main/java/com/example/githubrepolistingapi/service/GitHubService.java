package com.example.githubrepolistingapi.service;

import com.example.githubrepolistingapi.model.GitHubBranch;
import com.example.githubrepolistingapi.model.GitHubRepository;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GitHubService {
    Page<GitHubRepository> getUserRepositoriesPage(String username, int page, int size);
    List<GitHubRepository> fetchUserRepositories(String username);
    List<GitHubBranch> getRepositoryBranches(String username, String repoName);
    List<GitHubRepository> filterOutForks(List<GitHubRepository> list);
}
