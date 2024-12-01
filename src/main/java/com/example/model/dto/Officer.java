package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record Officer(
    String name,
    @JsonProperty("officer_role")
    String officerRole,
    @JsonProperty("appointed_on")
    String appointedOn,
    Address address,
    @JsonProperty("resigned_on")
    @JsonIgnore
    String resignedOn
)  {
}