package com.fullDetailed.fullDetailedDemo.util;

import com.fullDetailed.fullDetailedDemo.domain.dtos.Case.CaseRequestResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.UserResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.CaseRequests;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public class HelperDtoConverter {

    public static UserResponseDto mapToUserResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .role(user.getRole())
                .court(user.getCourt())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static CaseRequestResponseDto mapToCaseRequestDto(CaseRequests request) {
        return CaseRequestResponseDto.builder()
                .requestId(request.getId())
                .lawyerId(request.getLawyer().getId())
                .lawyerName(request.getLawyer().getFirstName() + " " + request.getLawyer().getLastName())
                .caseId(request.getLegalCase().getId())
                .caseNumber(request.getLegalCase().getCaseNumber())
                .status(request.getStatus().toString())
                .requestedAt(request.getCreatedAt())
                .build();
    }
}
