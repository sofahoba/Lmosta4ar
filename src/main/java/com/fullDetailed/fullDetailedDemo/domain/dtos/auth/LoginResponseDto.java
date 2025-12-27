package com.fullDetailed.fullDetailedDemo.domain.dtos.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fullDetailed.fullDetailedDemo.domain.enums.Role;
import lombok.Data;

import java.util.UUID;

@Data
@JsonPropertyOrder({ "access_token", "refresh_token", "id", "role" })
public class LoginResponseDto {
    private String access_token;
    private String refresh_token;
    private Role role;
    private UUID id;
}
