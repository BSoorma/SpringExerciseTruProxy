package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TruProxyCompany(
    @JsonProperty("company_number")
    String companyNumber,
    @JsonProperty("company_type")
    String companyType,
    String title,
    @JsonProperty("company_status")
    String companyStatus,
    @JsonProperty("date_of_creation")
    String dateOfCreation,
    Address address
) {
}
