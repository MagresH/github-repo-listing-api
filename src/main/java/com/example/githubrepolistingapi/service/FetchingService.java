package com.example.githubrepolistingapi.service;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface FetchingService {
    <T> List<T> fetchAllPages(String baseUrl, Class<T[]> responseType);

    boolean hasNextPage(HttpHeaders headers);

    HttpEntity<String> createHttpEntity();
}
