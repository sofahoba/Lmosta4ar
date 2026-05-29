package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefendantDto {
    private String name;
    private String alias;
    @JsonProperty("national_id")
    private String nationalId;
    private String gender;
    @JsonProperty("date_of_birth")
    private String dateOfBirth;
    private Integer age;
    private String occupation;
    private String nationality;
    private String address;
    @JsonProperty("complicity_role")
    private String complicityRole;
}