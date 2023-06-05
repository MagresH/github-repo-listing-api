package com.example.githubrepolistingapi.service;

import com.example.githubrepolistingapi.model.GitHubBranch;
import com.example.githubrepolistingapi.model.GitHubCommit;
import com.example.githubrepolistingapi.model.GitHubRepository;
import com.example.githubrepolistingapi.model.GitHubRepositoryOwner;
import com.example.githubrepolistingapi.service.implementation.GitHubServiceImpl;
import com.example.githubrepolistingapi.service.implementation.PaginationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
public class GitHubServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PaginationServiceImpl paginationService;

    @Mock
    private FetchingService fetchingService;

    @InjectMocks
    private GitHubServiceImpl gitHubService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gitHubService = new GitHubServiceImpl(paginationService, fetchingService);
    }

    @Test
    public void should_get_user_repositories_with_branches() {
        int pageNumber = 1;
        String username = "testuser";
        String repositoriesUrl = "https://api.github.com/users/" + username + "/repos" + "?page=" + pageNumber;
        String branchesUrl = "https://api.github.com/repos/" + username + "/repo1/branches";

        GitHubRepositoryOwner owner = new GitHubRepositoryOwner("login");

        GitHubRepository repository = new GitHubRepository("repo1", owner, false, null);
        GitHubRepository repository2 = new GitHubRepository("repo2", owner, true, null);
        GitHubRepository[] repositories = {repository, repository2};
        List<GitHubRepository> repositoryList = Arrays.asList(repositories);

        ResponseEntity<GitHubRepository[]> repositoriesResponse = ResponseEntity.ok().body(repositories);

        GitHubBranch branch = new GitHubBranch("branch1", new GitHubCommit("123456"));
        GitHubBranch branch2 = new GitHubBranch("branch2", new GitHubCommit("1234567"));
        GitHubBranch[] branches = {branch, branch2};
        List<GitHubBranch> branchList = Arrays.asList(branches);

        repository.setBranches(List.of(branches));

        ResponseEntity<GitHubBranch[]> branchesResponse = ResponseEntity.ok().body(branches);

        doReturn(branchList).when(fetchingService).fetchAllPages(branchesUrl, GitHubBranch[].class);
        doReturn(repositoryList).when(fetchingService).fetchAllPages(branchesUrl, GitHubBranch[].class);


        doReturn(repositoriesResponse).when(restTemplate).exchange(eq(repositoriesUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubRepository[].class));
        doReturn(branchesResponse).when(restTemplate).exchange(eq(branchesUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubBranch[].class));

        List<GitHubRepository> results = Arrays.asList(repositories).subList(0, 1);

        Page<GitHubRepository> page = new PageImpl<>(results, PageRequest.of(1, 30), results.size());

        doReturn(page).when(paginationService).paginate(any(), any());

        List<GitHubRepository> result = gitHubService.getUserRepositoriesPage(username, 1, 30).get().toList();

        assertEquals(1, result.size());
        assertEquals("repo1", result.get(0).getName());
        assertEquals("login", result.get(0).getOwner().getLogin());
        assertEquals("branch1", result.get(0).getBranches().get(0).getName());
        assertEquals("123456", result.get(0).getBranches().get(0).getCommit().getSha());

        assertFalse(result.get(0).isFork());
    }

    @Test
    public void should_get_repository_branches() {
        String username = "testuser";
        String repositoryName = "repo1";
        String branchesUrl = "https://api.github.com/repos/" + username + "/" + repositoryName + "/branches";

        GitHubBranch branch = new GitHubBranch("branch1", new GitHubCommit("123456"));
        GitHubBranch branch2 = new GitHubBranch("branch2", new GitHubCommit("1234567"));
        GitHubBranch[] branches = {branch, branch2};

        List<GitHubBranch> branchList = Arrays.asList(branches);
        ResponseEntity<GitHubBranch[]> branchesResponse = ResponseEntity.ok().body(branches);

        doReturn(branchList).when(fetchingService).fetchAllPages(branchesUrl, GitHubBranch[].class);
        doReturn(branchesResponse).when(restTemplate).exchange(eq(branchesUrl), eq(HttpMethod.GET), any(HttpEntity.class), eq(GitHubBranch[].class));

        List<GitHubBranch> result = gitHubService.fetchRepositoryBranches(username, repositoryName);

        assertEquals(2, result.size());
        assertEquals("branch1", result.get(0).getName());
        assertEquals("branch2", result.get(1).getName());
        assertEquals("123456", result.get(0).getCommit().getSha());
        assertEquals("1234567", result.get(1).getCommit().getSha());

    }

    @Test
    public void should_filter_forks() {
        GitHubRepository repo1 = new GitHubRepository("repo1", null, true, null);
        GitHubRepository repo2 = new GitHubRepository("repo2", null, false, null);
        GitHubRepository repo3 = new GitHubRepository("repo3", null, true, null);
        List<GitHubRepository> inputList = new ArrayList<>();
        inputList.add(repo1);
        inputList.add(repo2);
        inputList.add(repo3);

        List<GitHubRepository> result = gitHubService.filterOutForks(inputList);

        assertEquals(1, result.size());
        assertEquals("repo2", result.get(0).getName());
    }
}

