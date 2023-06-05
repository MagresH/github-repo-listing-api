package com.example.githubrepolistingapi.service;


import com.example.githubrepolistingapi.service.implementation.FetchingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class FetchingServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    private FetchingService fetchingService;
    private static final String GITHUB_TOKEN = "sampleGithubToken";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        fetchingService = new FetchingServiceImpl(restTemplate);
    }

    @Test
    public void should_fetch_all_pages() {
        String baseUrl = "https://api.github.com/users/octocat/repos";
        String[] mockResponse = {"repo1", "repo2"};
        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", "<https://api.github.com/users/repos?page=3>; rel=\"next\"");

        ResponseEntity<String[]> responseEntity = new ResponseEntity<>(mockResponse, headers, HttpStatus.OK);
        when(restTemplate.exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String[].class)))
                .thenReturn(responseEntity)
                .thenReturn(new ResponseEntity<>(new String[0], new HttpHeaders(), HttpStatus.OK));

        List<String> fetchedPages = fetchingService.fetchAllPages(baseUrl, String[].class);

        verify(restTemplate, times(2)).exchange(any(String.class), eq(HttpMethod.GET), any(HttpEntity.class), eq(String[].class));
        assertEquals(2, fetchedPages.size());
        assertTrue(fetchedPages.containsAll(Arrays.asList(mockResponse)));
    }


    @Test
    public void should_create_http_entity() {
        HttpEntity<String> httpEntity = fetchingService.createHttpEntity();
        assertNotNull(httpEntity);
    }


    @Test
    public void check_if_it_has_next_page() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Link", "<https://api.github.com/users/repos?page=2>; rel=\"next\"");

        assertTrue(fetchingService.hasNextPage(headers));

        headers = new HttpHeaders();
        headers.add("Link", "<https://api.github.com/users/repos?page=2>; rel=\"last\"");

        assertFalse(fetchingService.hasNextPage(headers));
    }
}
