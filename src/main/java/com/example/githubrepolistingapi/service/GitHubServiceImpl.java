package com.example.githubrepolistingapi.service;

import com.example.githubrepolistingapi.model.GitHubBranch;
import com.example.githubrepolistingapi.model.GitHubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GitHubServiceImpl implements GitHubService {

    private final RestTemplate restTemplate;
    private final PaginationService paginationService;

    @Value("${github.token}")
    private String GITHUB_TOKEN;


    // get repositories of given user and paginate them
    @Override
    public Page<GitHubRepository> getUserRepositoriesPage(String username, int page, int size) {

        List<GitHubRepository> repositories = fetchUserRepositories(username);

        return paginationService.paginate(repositories, Pageable.ofSize(size).withPage(page));
    }

    // fetch all repositories of given user from all pages
    @Override
    public List<GitHubRepository> fetchUserRepositories(String username) {

        HttpEntity<String> entity = createHttpEntity();
        int page = 1;
        HttpHeaders headers;
        List<GitHubRepository> repositoryList = new LinkedList<>();

        do {
            String url = "https://api.github.com/users/" + username + "/repos" + "?page=" + page;
            ResponseEntity<GitHubRepository[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, GitHubRepository[].class);
            System.out.println(responseEntity);
            headers = responseEntity.getHeaders();
            repositoryList.addAll(Arrays.asList(Objects.requireNonNull(responseEntity.getBody())));
            page++;
        } while (hasNextPage(headers));

        repositoryList = filterOutForks(repositoryList);

        linkRepositoryWithBranches(username, repositoryList);

        return repositoryList;
    }

    // link fetched branches with their repositories
    private void linkRepositoryWithBranches(String username, List<GitHubRepository> repositoryList) {
        repositoryList.forEach(repo -> {
            List<GitHubBranch> branches = getRepositoryBranches(username, repo.getName());
            repo.setBranches(branches);
        });
    }

    // get branches for a repository
    @Override
    public List<GitHubBranch> getRepositoryBranches(String username, String repoName) {

        HttpEntity<String> entity = createHttpEntity();

        String url = "https://api.github.com/repos/" + username + "/" + repoName + "/branches";

        ResponseEntity<GitHubBranch[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,GitHubBranch[].class);

        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    // check if token is set, if yes, add it to the request
    private HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (GITHUB_TOKEN.length()>0) headers.setBearerAuth(GITHUB_TOKEN);

        return new HttpEntity<>(null, headers);
    }

    // filter out forked repositories
    public List<GitHubRepository> filterOutForks(List<GitHubRepository> list) {
        return list.stream()
                .filter(x -> !x.isFork())
                .toList();
    }

    // check if there is a next page
    public boolean hasNextPage(HttpHeaders headers) {
        List<String> links = headers.get("Link");
        if (links == null) return false;
        for (String link : links) {
            if (link.contains("rel=\"next\"")) return true;
        }
        return false;
    }
}
