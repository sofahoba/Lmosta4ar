package com.fullDetailed.fullDetailedDemo.domain.dtos.ai;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IncidentDto {
    @JsonProperty("incident_type")
    private String incidentType;
    @JsonProperty("incident_date")
    private String incidentDate;
    @JsonProperty("incident_location")
    private String incidentLocation;
    @JsonProperty("incident_description")
    private String incidentDescription;
    @JsonProperty("perpetrator_names")
    private List<String> perpetratorNames;
    @JsonProperty("victim_names")
    private List<String> victimNames;
}