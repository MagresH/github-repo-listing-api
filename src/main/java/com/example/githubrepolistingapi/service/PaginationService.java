package com.example.githubrepolistingapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginationService {

    public <T> Page<T> paginate(List<T> items, Pageable pageable) {
        int totalItems = items.size();
        List<T> paginatedItems = getPageItems(items, pageable);
        return new PageImpl<>(paginatedItems, pageable, totalItems);
    }

    private <T> List<T> getPageItems(List<T> items, Pageable pageable) {
        int start = pageable.getPageNumber() * pageable.getPageSize();
        int end = Math.min(start + pageable.getPageSize(), items.size());
        if (start > end) {
            throw new IllegalArgumentException("Page index out of bounds"); // TODO: custom exception
        }
        return items.subList(start, end);
    }
}

