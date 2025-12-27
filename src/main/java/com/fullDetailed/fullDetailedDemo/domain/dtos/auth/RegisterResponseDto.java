package com.fullDetailed.fullDetailedDemo.domain.dtos.auth;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@JsonPropertyOrder({"id", "firstName", "lastName", "email", "age"})
public class RegisterResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private int age;
}
