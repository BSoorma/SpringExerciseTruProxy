package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Address(
    String locality,
    @JsonProperty("postal_code")
    String postalCode,
    String premises,
    @JsonProperty("address_line_1")
    String addressLine1,
    String country
) {
}
