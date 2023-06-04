package com.example.githubrepolistingapi.service;

import com.example.githubrepolistingapi.model.GitHubBranch;
import com.example.githubrepolistingapi.model.GitHubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService{
    @Override
    public List<GitHubRepository> getUserRepositories(String username) {
        return null;
    }

    @Override
    public List<GitHubBranch> getRepositoryBranches(String username, String repoName) {
        return null;
    }
}
