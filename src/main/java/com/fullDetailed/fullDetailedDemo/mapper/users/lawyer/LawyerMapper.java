package com.fullDetailed.fullDetailedDemo.mapper.users.lawyer;

import com.fullDetailed.fullDetailedDemo.domain.dtos.lawyer.LawyerDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public class LawyerMapper {

    public static LawyerDto toDto(User user){
        if(user == null) return null;

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

    public static User toEntity(LawyerDto dto){
        if(dto == null)return null;

        User user = new User();
        user.setId(dto.getId());
        user.setAge(dto.getAge());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setAssignedCasesCount(dto.getAssignedCasesCount());
        return user;
    }

    public static void updateEntity(User user, LawyerDto dto){
        if (dto.getAge() != null) {
            if (dto.getAge() < 25 || dto.getAge() > 70) {
                throw new IllegalArgumentException("Age must be between 25 and 70");
            }
            user.setAge(dto.getAge());
        }
        if(dto.getEmail() != null){
            user.setEmail(dto.getEmail());
        }
        if(dto.getFirstName() != null){
            user.setFirstName(dto.getFirstName());
        }
        if(dto.getLastName()!= null){
            user.setLastName(dto.getLastName());
        }
        if(dto.getAssignedCasesCount()!=null){
            user.setAssignedCasesCount(dto.getAssignedCasesCount());
        }
    }
}
