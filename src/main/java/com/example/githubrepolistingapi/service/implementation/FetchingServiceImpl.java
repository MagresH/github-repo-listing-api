package com.example.githubrepolistingapi.service.implementation;

import com.example.githubrepolistingapi.service.FetchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FetchingServiceImpl implements FetchingService {

    private final RestTemplate restTemplate;

    @Value("${github.token}")
    private String GITHUB_TOKEN;

    @Override
    public  <T> List<T> fetchAllPages(String baseUrl, Class<T[]> responseType) {
        List<T> dataList = new LinkedList<>();
        int page = 1;
        ResponseEntity<T[]> responseEntity;
        HttpHeaders headers;

        do {
            String url = baseUrl + "?page=" + page;
            responseEntity = restTemplate.exchange(url, HttpMethod.GET, createHttpEntity(), responseType);
            headers = responseEntity.getHeaders();
            Collections.addAll(dataList, Objects.requireNonNull(responseEntity.getBody()));
            page++;
        } while (hasNextPage(headers));

        return dataList;
    }

    @Override
    public HttpEntity<String> createHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        if (GITHUB_TOKEN != null && GITHUB_TOKEN.length() > 0) headers.setBearerAuth(GITHUB_TOKEN);

        return new HttpEntity<>(null, headers);
    }

    @Override
    public boolean hasNextPage(HttpHeaders headers) {
        List<String> links = headers.get("Link");
        if (links == null) return false;
        for (String link : links) {
            if (link.contains("rel=\"next\"")) return true;
        }
        return false;
    }

}
