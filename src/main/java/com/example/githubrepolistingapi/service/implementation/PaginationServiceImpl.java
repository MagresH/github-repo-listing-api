package com.example.githubrepolistingapi.service.implementation;

import com.example.githubrepolistingapi.service.PaginationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaginationServiceImpl implements PaginationService {

    public <T> Page<T> paginate(List<T> items, Pageable pageable) {
        int totalItems = items.size();
        List<T> paginatedItems = getPageItems(items, pageable);
        return new PageImpl<>(paginatedItems, pageable, totalItems);
    }

    // get items for given page, pages start from index 1 like in a book
    public  <T> List<T> getPageItems(List<T> items, Pageable pageable) {
        int start = (pageable.getPageNumber() - 1 )* pageable.getPageSize();
        if (start > items.size()) {
            return new ArrayList<>();
        }
        int end = Math.min(start + pageable.getPageSize(), items.size());

        return items.subList(start, end);
    }
}


