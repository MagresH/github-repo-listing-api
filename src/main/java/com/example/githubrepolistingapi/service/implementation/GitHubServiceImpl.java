package com.example.githubrepolistingapi.service.implementation;

import com.example.githubrepolistingapi.model.GitHubBranch;
import com.example.githubrepolistingapi.model.GitHubRepository;
import com.example.githubrepolistingapi.service.FetchingService;
import com.example.githubrepolistingapi.service.GitHubService;
import com.example.githubrepolistingapi.service.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {

    private final PaginationService paginationService;
    private final FetchingService fetchingService;

    @Override
    public Page<GitHubRepository> getUserRepositoriesPage(String username, int page, int size) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        List<GitHubRepository> repositories = fetchUserRepositories(username);

        return paginationService.paginate(repositories, Pageable.ofSize(size).withPage(page));
    }

    @Override
    public List<GitHubRepository> fetchUserRepositories(String username) {
        String baseUrl = "https://api.github.com/users/" + username + "/repos";

        List<GitHubRepository> repositoryList = fetchingService.fetchAllPages(baseUrl, GitHubRepository[].class);

        repositoryList = filterOutForks(repositoryList);

        linkRepositoriesWithBranches(username, repositoryList);

        return repositoryList;
    }

    @Override
    public List<GitHubBranch> fetchRepositoryBranches(String username, String repoName) {

        String baseUrl = "https://api.github.com/repos/" + username + "/" + repoName + "/branches";

        return fetchingService.fetchAllPages(baseUrl, GitHubBranch[].class);
    }

    @Override
    public void linkRepositoriesWithBranches(String username, List<GitHubRepository> repositoryList) {
        repositoryList.forEach(repo -> {
            List<GitHubBranch> branches = fetchRepositoryBranches(username, repo.getName());
            repo.setBranches(branches);
        });
    }

    @Override
    public List<GitHubRepository> filterOutForks(List<GitHubRepository> list) {
        return list.stream()
                .filter(x -> !x.isFork())
                .toList();
    }
}
