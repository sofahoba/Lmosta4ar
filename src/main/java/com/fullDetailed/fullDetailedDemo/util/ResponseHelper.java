package com.fullDetailed.fullDetailedDemo.util;

import com.fullDetailed.fullDetailedDemo.domain.dtos.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> ok(List<T> data, String message) {
        return ResponseEntity.ok(ApiResponse.<List<T>>builder()
                .success(true)
                .message(message)
                .data(data)
                .count(data.size())
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> ok(Page<T> page, String message) {
        return ResponseEntity.ok(ApiResponse.<List<T>>builder()
                .success(true)
                .message(message)
                .data(page.getContent())
                .count(page.getContent().size())
                .pageInfo(ApiResponse.PageInfo.builder()
                        .currentPage(page.getNumber())
                        .totalPages(page.getTotalPages())
                        .totalElements(page.getTotalElements())
                        .pageSize(page.getSize())
                        .hasNext(page.hasNext())
                        .hasPrevious(page.hasPrevious())
                        .build())
                .build());
    }

    public static ResponseEntity<ApiResponse<Void>> ok(String message) {
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<T>builder()
                        .success(true)
                        .message(message)
                        .data(data)
                        .build());
    }

    public static ResponseEntity<ApiResponse<Void>> noContent(String message) {
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<Page<T>>> okPage(Page<T> page, String message) {
        return ResponseEntity.ok(ApiResponse.<Page<T>>builder()
                .success(true)
                .message(message)
                .data(page)
                .build());
    }

}
