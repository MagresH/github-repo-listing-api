package com.example.githubrepolistingapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaginationService {
    <T> List<T> getPageItems(List<T> items, Pageable pageable);

    <T> Page<T> paginate(List<T> items, Pageable pageable);
}
