package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiInvokeRequest {

    private String case_id;

    private List<String> source_documents;
}