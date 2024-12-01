package com.example.model.response;

import com.example.model.dto.Company;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import org.springframework.validation.annotation.Validated;

@Validated
public record CompanyResponse(
    @JsonProperty("total_results")
    int totalResults,
    List<Company> items) implements Serializable {
}
