package com.fullDetailed.fullDetailedDemo.domain.dtos;

import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserProfileResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private Role role;
    private Boolean isActive;
    private int assignedCasesCount;
    private String court;
    private Boolean isApproved;
}