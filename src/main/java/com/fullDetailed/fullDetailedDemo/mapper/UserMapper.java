package com.fullDetailed.fullDetailedDemo.mapper;

import com.fullDetailed.fullDetailedDemo.domain.dtos.UserProfileResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public class UserMapper {

    public static UserProfileResponseDto toDto(User user) {
        return UserProfileResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .role(user.getRole())
                .isActive(user.isActive())
                .assignedCasesCount(user.getAssignedCasesCount())
                .court(user.getCourt())
                .isApproved(user.isApproved())
                .build();
    }
}
