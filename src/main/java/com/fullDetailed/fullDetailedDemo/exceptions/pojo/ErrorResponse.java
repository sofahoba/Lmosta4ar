package com.fullDetailed.fullDetailedDemo.exceptions.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;
    private String path;
    private LocalDateTime timestamp;

}
