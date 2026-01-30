package com.fullDetailed.fullDetailedDemo.mapper.users.lawyer;

import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public class LawyerMapper {

    public static LawyerDto toDto(User user) {
        return LawyerDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .isActive(user.isActive())
                .isApproved(user.isApproved())
                .assignedCasesCount(user.getAssignedCasesCount())
                .build();
    }

    public static void updateEntity(User user, LawyerDto dto) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
    }
}
