package com.fullDetailed.fullDetailedDemo.domain.dtos.judge;

import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Role role;
    private String court;
    private boolean isActive;
    private LocalDateTime createdAt;
}
