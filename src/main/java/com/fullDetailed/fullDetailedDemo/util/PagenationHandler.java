package com.fullDetailed.fullDetailedDemo.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PagenationHandler {
    public static Pageable createCleanPageable(Pageable pageable) {
        return PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize()
        );
    }
}
