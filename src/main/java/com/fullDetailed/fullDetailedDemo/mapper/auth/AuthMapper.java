package com.fullDetailed.fullDetailedDemo.mapper.auth;


import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterRequestDto;
import com.fullDetailed.fullDetailedDemo.domain.dtos.auth.RegisterResponseDto;
import com.fullDetailed.fullDetailedDemo.domain.entities.User;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(RegisterRequestDto dto){
        return User.builder()
                .age(dto.getAge())
                .email(dto.getEmail())
                .role(Role.JUDGE)
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .assignedCasesCount(0)
                .isActive(false)
                .build();
    }

    public RegisterResponseDto toDto(User user){
        return RegisterResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .age(user.getAge())
                .build();
    }
}
