package com.fullDetailed.fullDetailedDemo.mapper.users.judge;

import com.fullDetailed.fullDetailedDemo.domain.dtos.judge.JudgeProfileDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

public class JudgeMapper {

    public static JudgeProfileDto toDto(User user){
        if(user == null)return null;

        JudgeProfileDto dto = new JudgeProfileDto();
        dto.setId(user.getId());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAssignedCasesCount(user.getAssignedCasesCount());

        return dto;
    }

    public static User toEntity(JudgeProfileDto dto){
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

    public static void updateEntity(User user, JudgeProfileDto dto){
        if(dto.getAge() != 0) {
            if(dto.getAge() < 25 || dto.getAge() > 70) {
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
            user.setFirstName(dto.getLastName());
        }
    }
}
