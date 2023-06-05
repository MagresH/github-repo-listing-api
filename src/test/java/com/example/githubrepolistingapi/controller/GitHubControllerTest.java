package com.example.githubrepolistingapi.controller;

import com.example.githubrepolistingapi.exception.GlobalExceptionHandler;
import com.example.githubrepolistingapi.model.GitHubBranch;
import com.example.githubrepolistingapi.model.GitHubCommit;
import com.example.githubrepolistingapi.model.GitHubRepository;
import com.example.githubrepolistingapi.model.GitHubRepositoryOwner;
import com.example.githubrepolistingapi.service.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class GitHubControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GitHubService gitHubService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        GitHubController gitHubController = new GitHubController(gitHubService);
        mockMvc = MockMvcBuilders.standaloneSetup(gitHubController).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    public void should_return_user_repositories_on_get_request() throws Exception {

        String username = "john";
        List<GitHubRepository> repositories = new ArrayList<>();

        GitHubRepositoryOwner owner = new GitHubRepositoryOwner(username);

        GitHubRepository repository = new GitHubRepository("repo1", owner, false, null);
        GitHubRepository repository2 = new GitHubRepository("repo2", owner, true, null);

        GitHubBranch branch = new GitHubBranch("master", new GitHubCommit("123456"));
        GitHubBranch branch2 = new GitHubBranch("dev", new GitHubCommit("1234567"));
        List<GitHubBranch> branches = new ArrayList<>(Arrays.asList(branch, branch2));

        repository.setBranches(branches);
        repository2.setBranches(branches);

        repositories.add(repository);
        repositories.add(repository2);



        when(gitHubService.getUserRepositoriesPage(username,1,30)).thenReturn(new PageImpl<>(repositories.subList(0,1)));



        mockMvc.perform(get("/api/v1/github/user/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].name").value("repo1"))
                .andExpect(jsonPath("$.content[0].owner.login").value(owner.getLogin()))
                .andExpect(jsonPath("$.content[0].branches[0].name").value("master"))
                .andExpect(jsonPath("$.content[0].branches[0].commit.sha").value("123456"))
                .andExpect(jsonPath("$.content[0].branches[1].name").value("dev"))
                .andExpect(jsonPath("$.content[0].branches[1].commit.sha").value("1234567"))
                .andExpect(jsonPath("$.content[0].branches[0].commit.sha").value("123456"));
    }


    @Test
    void should_return_not_found_when_user_does_not_exist() throws Exception {

        String username = "nonexistent";

        when(gitHubService.getUserRepositoriesPage(username,1,30))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "User with given username not found"));

        mockMvc.perform(get("/api/v1/github/user/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof HttpClientErrorException))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User with given username not found"));
    }

    @Test
    void should_return_not_acceptable_when_invalid_accept_header() throws Exception {

        String username = "john";

        mockMvc.perform(get("/api/v1/github/user/{username}", username)
                        .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.status").value(406))
                .andExpect(jsonPath("$.message").value("Client must accept application/json"));
    }

    @Test
    void should_return_valid_header_on_get_request() throws Exception {

        String username = "john";

        mockMvc.perform(get("/api/v1/github/user/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}