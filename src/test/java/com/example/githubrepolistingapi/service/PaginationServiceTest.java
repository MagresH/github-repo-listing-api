package com.example.githubrepolistingapi.service;

import com.example.githubrepolistingapi.service.implementation.PaginationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaginationServiceTest {

    @Test
    public void should_paginate_list() {

        PaginationServiceImpl paginationService = new PaginationServiceImpl();

        List<String> items = new ArrayList<>();
        for(int i = 1; i <= 50; i++) {
            items.add("Item" + i);
        }

        Pageable pageable = PageRequest.of(1, 10);
        Page<String> page = paginationService.paginate(items, pageable);

        for(int i = 0; i < page.getContent().size(); i++) {
            assertEquals("Item" + (i+1), page.getContent().get(i));
        }

        assertEquals(1, page.getNumber());
        assertEquals(10, page.getNumberOfElements());
        assertEquals(10, page.getSize());
        assertEquals(5, page.getTotalPages());
        assertEquals(50, page.getTotalElements());
    }
}
